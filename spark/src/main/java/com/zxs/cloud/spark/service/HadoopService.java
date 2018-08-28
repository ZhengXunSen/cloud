package com.zxs.cloud.spark.service;

import org.apache.hadoop.fs.Path;

/**
 * Created by bill.zheng in 2018/8/28
 */
public interface HadoopService {

    boolean isExist(Path path);

    boolean deleteFile(Path path);

    void writeFileAppend(String content, Path path);

    void writeNewFile(String content, Path path);

    void readFile(Path path);
}
