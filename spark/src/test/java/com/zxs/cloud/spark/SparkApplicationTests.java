package com.zxs.cloud.spark;

import com.zxs.cloud.spark.service.SparkService;
import com.zxs.cloud.spark.util.JavaSparkContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.spark.SparkConf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class SparkApplicationTests {

    @Autowired
    private JavaSparkContextUtil javaSparkContextUtil;
    @Value("${spark.master}")
    private String sparkMaster;
    @Autowired
    private SparkService sparkService;

    @Test
    public void testSparkConfig() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://zxs-1:9000");
        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
       /* Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://testcluster");
        conf.set("dfs.nameservices","testcluster"); //假设nameservices命名为testcluster
        conf.set("dfs.ha.namenodes.testcluster","nn1,nn2");
        conf.set("dfs.namenode.rpc-address.testcluster.nn1","192.168.**.**:8020");
        conf.set("dfs.namenode.rpc-address.testcluster.nn2","192.168.**.**:8020");
        conf.set("dfs.client.failover.proxy.provider.testcluster","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        FileSystem fs = FileSystem.get(conf);*/
        FileSystem fs = FileSystem.get(new URI("hdfs://zxs-1:9000"),conf, "hadoop");
        Path path = new Path("/app/hadoop-3.1.0/dataDir/hdfs/data/b.txt");
        if (fs.exists(path)){
            System.out.println("hdfs上存在该文件");
            fs.delete(path, true);
        }
        FSDataOutputStream fsDataOutputStream = fs.create(path);
        String s = "这是我测试hdfs用的";
        fsDataOutputStream.write(s.getBytes());
        fsDataOutputStream.flush();
        fsDataOutputStream.close();
        FSDataInputStream fsDataInputStream = fs.open(new Path("/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml"));
        IOUtils.copyBytes(fsDataInputStream, System.out, 4096, false);
        IOUtils.closeStream(fsDataInputStream);
        fsDataInputStream.close();
//        FileSystem fs = FileSystem.get(new URI("hdfs://10.3.20.126:9000"),conf, "hadoop");
//        boolean result = fs.exists(new Path("/app/hadoop-3.1.0/dataDir/hdfs/data/b.txt"));
//        System.out.println("服务器上是否存在文件：" + result);
//        try (FSDataOutputStream fsDataOutputStream = fs.create(new Path("/app/hadoop-3.1.0/dataDir/hdfs/data/b.txt", String.valueOf(true)))) {
//            System.out.println("执行了创建");
//        }
//        boolean result = fs.mkdirs(new Path("/app/hadoop-3.1.0/dataDir/hdfs/data/b.txt"));
//        System.out.println(result);
//        fs.access(new Path("/app/hadoop-3.1.0/dataDir/hdfs/data/a.txt"), FsAction.READ);
//        try (JavaSparkContext javaSparkContext = javaSparkContextUtil.getJavaSparkContext("spark")){
//            JavaRDD<String> lines = javaSparkContext.textFile("hdfs://zxs-1:9000//app/hadoop-3.1.0/dataDir/hdfs/data/b.txt");
//            System.out.println("文本内词的数量：" + lines.count());
//        }
        sparkService.fileReader(new SparkConf());
    }



    @Test
    public void testSparkOperation(){
        sparkService.operateRDD(new SparkConf());
    }
}
