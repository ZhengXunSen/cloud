package com.zxs.cloud.spark.leaning;

import com.zxs.cloud.spark.util.JavaSparkContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

@Slf4j
public class SparkOperation {

    public static void main(String[] args) {
        //读取文件操作
        fileReader();
        //计算
        calculator();
    }

    /**
     * 读取文件操作
     */
    public static void fileReader(){
        JavaSparkContext sc = JavaSparkContextUtil.getJavaSparkContext("zxs");
        JavaRDD<String> lines = sc.textFile("C:/Users/lenovo/Desktop/new 1.txt");
        log.debug("文件的第一行为:" + lines.first());
        //包含操作
        JavaRDD<String> errRdd = lines.filter(line -> line.contains("error"));
        JavaRDD<String> warnRdd = lines.filter(line -> line.contains("warning"));
        //联合操作
        JavaRDD<String> badRdd = errRdd.union(warnRdd);
        log.debug("错误的行数：{}，警告的行数：{}，错误和警告的行数：{}", errRdd.count(), warnRdd.count(), badRdd.count());
        badRdd.take(3).parallelStream().forEach(line -> log.debug("获取的前三行中一行的内容为：{}", line));
        errRdd.saveAsObjectFile("E:/spark-dir/error.txt");
        sc.close();
    }

    public static void calculator(){
        JavaSparkContext sc = JavaSparkContextUtil.getJavaSparkContext("calculator");
        JavaRDD<Integer> integerJavaRDD = sc.parallelize(Arrays.asList(1,2,3,4,5));
        JavaRDD<Integer> result = integerJavaRDD.map(x -> x*x);
        log.debug("计算结果：{}", StringUtils.join(result.collect(), ","));
        sc.close();
    }
}
