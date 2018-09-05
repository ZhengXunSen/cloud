package com.zxs.cloud.spark.service.impl;

import com.zxs.cloud.spark.config.KafkaConfig;
import com.zxs.cloud.spark.config.SparkConfig;
import com.zxs.cloud.spark.service.SparkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by bill.zheng in 2018/8/22
 */
@Service
@Slf4j
public class SparkServiceImpl implements SparkService {

    @Value("${spring.kafka.test.topic}")
    private String topics;

    @Autowired
    private KafkaConfig kafkaConfig;

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
        try (JavaSparkContext javaSparkContext = new JavaSparkContext(sparkSession.sparkContext())) {
            //设置hadoop配置
            javaSparkContext.hadoopConfiguration().set("fs.defaultFS", "hdfs://zxs-1:9000");
            javaSparkContext.hadoopConfiguration().set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            javaSparkContext.hadoopConfiguration().set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
            //cache将rdd内容缓存起来，实际场景不建议
            JavaRDD<String> lines = javaSparkContext.textFile("/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml").cache();
            //map转换操作，和lambada表达式map作用相同
            lines.collect().stream().map(s -> s.concat("bill.zheng 测试")).forEach(s -> log.info("转换后的字是:{}", s));
        }
    }

    @Override
    @SparkConfig(executorMemory = "1500m", appName = "kafka")
    public void sparkStreaming(SparkConf sparkConf) throws InterruptedException {
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        sc.setLogLevel("WARN");
        JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(1));

        Collection<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        //kafka相关参数，必要！缺了会报错
        Map<String, Object> kafkaParams = kafkaConfig.getKafkaParam();
        //Topic分区
        Map<TopicPartition, Long> offsets = new HashMap<>();
        offsets.put(new TopicPartition(topics, 0), 2L);
//        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sc, Durations.seconds(1));
//        javaStreamingContext.textFileStream("/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml");

        //通过KafkaUtils.createDirectStream(...)获得kafka数据，kafka相关参数由kafkaParams指定
        JavaInputDStream<ConsumerRecord<Object, Object>> lines;
        lines = KafkaUtils.createDirectStream(
                ssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams, offsets)
        );
        //这里就跟之前的demo一样了，只是需要注意这边的lines里的参数本身是个ConsumerRecord对象
//        JavaPairDStream<String, Integer> counts =
//                lines.flatMap(x -> Arrays.asList(x.value().toString().split(" ")).iterator())
//                        .mapToPair(x -> new Tuple2<String, Integer>(x, 1))
//                        .reduceByKey((x, y) -> x + y);
//        counts.print();
//  可以打印所有信息，看下ConsumerRecord的结构
        lines.foreachRDD(rdd -> {
            rdd.foreach(x -> {
                log.info("查询到的值为:{}", x);
            });
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
}
