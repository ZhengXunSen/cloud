package com.zxs.cloud.spark.service.impl;

import com.zxs.cloud.spark.config.SparkConfig;
import com.zxs.cloud.spark.service.SparkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

/**
 * Created by bill.zheng in 2018/8/22
 */
@Service
@Slf4j
public class SparkServiceImpl implements SparkService {


    @Override
    @SparkConfig(executorMemory = "1500m", sparkMaster = "local[1]", appName = "fileReader")
    public String fileReader(SparkConf sparkConf) {
        String s = "文本内词的数量：";
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        try (JavaSparkContext javaSparkContext =  new JavaSparkContext(sparkSession.sparkContext())){
            javaSparkContext.hadoopConfiguration().set("fs.defaultFS", "hdfs://zxs-1:9000");
            javaSparkContext.hadoopConfiguration().set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
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
        try (JavaSparkContext javaSparkContext =  new JavaSparkContext(sparkSession.sparkContext())){
            //设置hadoop配置
            javaSparkContext.hadoopConfiguration().set("fs.defaultFS", "hdfs://zxs-1:9000");
            javaSparkContext.hadoopConfiguration().set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
            javaSparkContext.hadoopConfiguration().set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
            //cache将rdd内容缓存起来，实际场景不建议
            JavaRDD<String> lines = javaSparkContext.textFile("/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml").cache();
            //map转换操作，和lambada表达式map作用相同
            lines.collect().stream().map(s -> s.concat("bill.zheng 测试")).forEach(s -> log.info("转换后的字是:{}",s));
        }
    }
}
