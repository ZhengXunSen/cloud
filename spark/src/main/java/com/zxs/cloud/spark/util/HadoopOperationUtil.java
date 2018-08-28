package com.zxs.cloud.spark.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by bill.zheng in 2018/8/22
 */
@Slf4j
public class HadoopOperationUtil {

    private HadoopOperationUtil(){}

    public static FileSystem getFileSystem(String hdfsUrl, String userName){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(hdfsUrl),conf, userName);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            log.error("文件流创建错误",e);
        }
        return fs;
    }
}
