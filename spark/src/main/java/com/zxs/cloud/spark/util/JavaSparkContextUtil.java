package com.zxs.cloud.spark.util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by bill.zheng in 2018/8/10
 */
@Component
public class JavaSparkContextUtil {
    @Value("${spark.master}")
    private String master;

    public JavaSparkContext getJavaSparkContext(String appName){
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName(appName);
        return new JavaSparkContext(sparkConf);
    }
}
