package com.zxs.cloud.spark.spark;

import com.zxs.cloud.spark.service.SparkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bill.zheng in 2018/9/4
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class SparkTest {

    @Autowired
    private SparkService sparkService;
    @Test
    public void testSparkKafkaStreaming(){
        try {
            sparkService.sparkStreaming(new SparkConf());
        } catch (InterruptedException e) {
            log.error("执行错误", e);
            Thread.currentThread().interrupt();
        }
    }
}
