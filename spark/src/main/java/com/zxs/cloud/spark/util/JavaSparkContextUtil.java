package com.zxs.cloud.spark.util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by bill.zheng in 2018/8/10
 */
public class JavaSparkContextUtil {

    private JavaSparkContextUtil(){}

    public static JavaSparkContext getJavaSparkContext(String appName){
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName(appName);
        return new JavaSparkContext(sparkConf);
    }
}
