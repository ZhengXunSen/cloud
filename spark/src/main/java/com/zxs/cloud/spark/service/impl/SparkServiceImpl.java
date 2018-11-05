package com.zxs.cloud.spark.service.impl;

import com.zxs.cloud.spark.config.KafkaConfig;
import com.zxs.cloud.spark.config.SparkConfig;
import com.zxs.cloud.spark.dto.MessageDto;
import com.zxs.cloud.spark.model.User;
import com.zxs.cloud.spark.service.SparkService;
import com.zxs.cloud.spark.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.deploy.SparkSubmit;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by bill.zheng in 2018/8/22
 */
@Service
@Slf4j
public class SparkServiceImpl implements SparkService {

    @Value("${spring.kafka.test.topic}")
    private String topics;

    @Value("${spring.datasource.url}")
    private String dataUrl;

    @Value("${spring.datasource.username}")
    private String dataUser;

    @Value("${spring.datasource.password}")
    private String dataPwd;

    @Value("${spring.datasource.driver-class-name}")
    private String dataDriver;

    @Autowired
    private KafkaConfig kafkaConfig;

    private Properties properties = new Properties();

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @PostConstruct
    public void init(){
        properties.setProperty("user", dataUser);
        properties.setProperty("password", dataPwd);

    }

    @Override
    @SparkConfig(executorMemory = "1500m", sparkMaster = "local[1]", appName = "fileReader")
    public String fileReader(SparkConf sparkConf) {
        String s = "文本内词的数量：";
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        try (JavaSparkContext javaSparkContext = new JavaSparkContext(sparkSession.sparkContext())) {
            javaSparkContext.hadoopConfiguration().set("fs.defaultFS", "hdfs://zxs-1:9000");
            javaSparkContext.hadoopConfiguration().set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            javaSparkContext.hadoopConfiguration().set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
            JavaRDD<String> lines = javaSparkContext.textFile("/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml").cache();
            lines.first();
            lines.collect().stream().forEach(log::info);
        }
        log.info(s);
        return s;
    }

    @Override
    @SparkConfig(executorMemory = "1500m", sparkMaster = "local[2]", appName = "fileReader")
    public void operateRDD(SparkConf sparkConf) {
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        try (JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext())) {
            //设置hadoop配置
            /*javaSparkContext.hadoopConfiguration().set("fs.defaultFS", "hdfs://zxs-1:9000");
            javaSparkContext.hadoopConfiguration().set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            javaSparkContext.hadoopConfiguration().set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
            //cache将rdd内容缓存起来，实际场景不建议
            JavaRDD<String> lines = javaSparkContext.textFile("/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml").cache();
            //map转换操作，和lambada表达式map作用相同
            lines.collect().stream().map(s -> s.concat("bill.zheng 测试")).forEach(s -> log.info("转换后的字是:{}", s));*/
            JavaPairRDD javaRDD = sc.parallelize(Arrays.asList("a,b,b,c,c".split(","))).mapToPair(x -> new Tuple2<String, Integer>(x,1)).reduceByKey((x, y) -> x+y);
           log.warn("统计结果：" + javaRDD.collect());
        }
    }

    @Override
    @SparkConfig(executorMemory = "1500m", sparkMaster = "local[2]",appName = "kafka", sparkUser = "hadoop")
    public void sparkStreaming(SparkConf sparkConf) {
        sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "10");
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());
        sc.setLogLevel("WARN");
        JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(10));
        //kafka相关参数，必要！缺了会报错
        Map<String, Object> kafkaParams = kafkaConfig.getKafkaParam();
        //Topic分区
        List<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartition(topics, 0));
        topicPartitions.add(new TopicPartition(topics, 1));
        topicPartitions.add(new TopicPartition(topics, 2));

        //通过KafkaUtils.createDirectStream(...)获得kafka数据，kafka相关参数由kafkaParams指定
        JavaInputDStream<ConsumerRecord<Object, Object>> lines = KafkaUtils.createDirectStream(ssc,LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Assign(topicPartitions,kafkaParams));
        JavaDStream<User> javaDStream = lines.map(ConsumerRecord::value).map(value -> {
            MessageDto messageDto = JsonUtils.parse(value.toString(), MessageDto.class);
            return JsonUtils.parse(messageDto.getData(), User.class);
        });
        JavaPairDStream<String, Integer> counts =
                lines.flatMap(x -> Arrays.asList(x.value().toString().split(" ")).iterator())
                        .mapToPair(x -> new Tuple2<String, Integer>(x, 1))
                        .reduceByKey((x, y) -> x + y);
        counts.foreachRDD(count -> log.warn("统计的结果如下：{}", count.collect()));
        //处理消息，插入hive
        javaDStream.foreachRDD(rdd ->{
            Dataset<Row> dataset = sparkSession.createDataFrame(rdd, User.class);
            log.warn("测试：{}" , rdd.collect());
            dataset.show(1);
            dataset.createOrReplaceTempView("temp");
            String insertSQL = "insert into table test " +
                    "select id,userName from temp";
            sparkSession.sql("use default");
            sparkSession.sql(insertSQL);
//            dataset.write().mode(SaveMode.Append).jdbc(dataUrl,"user", properties);
        });

        ssc.start();
        try {
            ssc.awaitTermination();
        } catch (InterruptedException e) {
            log.error("执行出错", e);
            Thread.currentThread().interrupt();
        }
        ssc.close();
    }

    @Override
    @SparkConfig(executorMemory = "1500m", sparkMaster = "spark://zxs-1:7077",appName = "hive")
    public void sparkSql(SparkConf sparkConf) {
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate();
//        sparkSession.sql("insert into default.test values(3,'dc')").writeStream();
        sparkSession.sql("select * from default.test").show();
    }

    @Override
    @SparkConfig(executorMemory = "1500m", sparkMaster = "local[2]",appName = "mysql")
    public void sparkSqlOpMysql(SparkConf sparkConf) {
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        DataFrameReader dataFrame = sparkSession.read().format("jdbc").option("driver", dataDriver).option("url", dataUrl).option("user", dataUser).option("password", dataPwd);
        dataFrame.option("dbtable", "user").load();
        SparkContext sc = sparkSession.sparkContext();
        /*Arrays.stream(sc.textFile("C:\\Users\\bill.zheng\\Desktop\\a.txt",10).collect()).map(s -> s.split(",")).map(s -> {
            User user = new User();
            user.setId(3L);
            user.setUserName("dc");
            return user;
        });*/
        sparkSession.sql("select * from default.test").show();
    }


    @Override
    public void sparkSubmit() {
        String[] arg0=new String[]{
                "--master","spark://zxs-1:7077",
                "--class","org.apache.spark.examples.SparkPi",
                "--name","web polling",
                "--executor-memory","512m",
                "--total-executor-cores", "1",
                "hdfs://zxs-1:9000/app/hadoop-3.1.0/dataDir/hdfs/data/spark-examples_2.11-2.3.1.jar",
        };
        log.warn("提交作业...");
        SparkSubmit.main(arg0);
    }
}
