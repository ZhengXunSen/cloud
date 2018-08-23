package com.zxs.cloud.spark.service;

import org.apache.spark.SparkConf;

/**
 * Created by bill.zheng in 2018/8/22
 */
public interface SparkService {

    String fileReader(SparkConf sparkConf);
}
