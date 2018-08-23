package com.zxs.cloud.spark.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by bill.zheng in 2018/8/10
 */
@Component
@Aspect
@Slf4j
public class SparkConfigAspect {

    private SparkConfig sparkConfig;
    private SparkConf sparkConf;
    @Value("${spark.master}")
    private String master;

    private static final String LOCAL = "local";

    @Pointcut("@annotation(com.zxs.cloud.spark.config.SparkConfig)")
    public void pointCut() {
        throw new UnsupportedOperationException();
    }

    @Around("pointCut()")
    public void doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        sparkConfig = getAnnotation(proceedingJoinPoint);
        sparkConf = new SparkConf();
        initSparkConf();
        log.info("spark 配置初始化,主机IP:{},app名称:{},最大核心数:{},运行内存:{},是否可打印log:{},jar包执行路径:{},日志路径:{}",
                getMaster(), sparkConfig.appName(), sparkConfig.maxCores(), sparkConfig.executorMemory(),
                sparkConfig.logEnable(), sparkConfig.jars(), sparkConfig.logDir());
        setJavaSparkContextAndProceed(proceedingJoinPoint, sparkConf);
    }

    private SparkConfig getAnnotation(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(SparkConfig.class);
    }

    private void setJavaSparkContextAndProceed(ProceedingJoinPoint joinPoint, SparkConf sparkConf) throws Throwable {
        boolean haveNessaryArg = false;
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof SparkConf) {
                args[i]  = sparkConf;
                joinPoint.proceed(args);
                haveNessaryArg = true;
            }
        }
        if (!haveNessaryArg) {
            log.error("缺少JavaSparkContext参数初始化环境");
            throw new Exception();
        }
    }

    private void initSparkConf() {
        sparkConf.setAppName(sparkConfig.appName());
        sparkConf.set("spark.cores.max", sparkConfig.maxCores());
        sparkConf.set("spark.executor.memory", sparkConfig.executorMemory());
        sparkConf.set("spark.eventLog.enabled", sparkConfig.logEnable());
        setMaster();
        setJars();
        setLogDir();
        sparkConf.set("spark.driver.allowMultipleContexts",sparkConfig.allowMultipleContexts());
    }

    private void setJars() {
        String[] jars = sparkConfig.jars();
        if (jars.length != 0) {
            sparkConf.setJars(jars);
        }
    }

    private void setLogDir() {
        String logDir = sparkConfig.logDir();
        if (!logDir.isEmpty()) {
            sparkConf.set("spark.eventLog.dir", logDir);
        }
    }

    private void setMaster() {
        sparkConf.setMaster(getMaster());
    }

    private String getMaster(){
        String sparkMaster = sparkConfig.sparkMaster();
        //如果注释中有定义值，则使用注释中自定的值
        if (!sparkMaster.equals(LOCAL))
            return sparkMaster;
            //如果用户未在注解中定义值，则从配置文件中读取master信息
        else if (Objects.nonNull(master))
            return master;
            //如果用户既没有自定义master也没在配置文件中配置，使用默认值（单机配置）
        else
            return sparkMaster;
    }
}
