package com.zxs.cloud.spark.controller;

import com.zxs.cloud.spark.service.SparkService;
import org.apache.spark.SparkConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bill.zheng in 2018/8/10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SparkService sparkService;

    @RequestMapping("testSpark")
    public String testSpark(){
        SparkConf sparkConf = new SparkConf();
        return sparkService.fileReader(sparkConf);
    }
}
