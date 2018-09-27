package com.zxs.cloud.spark.config;

import java.lang.annotation.*;

/**
 * Created by bill.zheng in 2018/8/10
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SparkConfig {

    String sparkMaster() default "local";

    String appName() default "spark";

    String[] jars() default "";

    String maxCores() default "1";

    String executorMemory() default "512m";

    String logEnable() default "true";

    String logDir() default "";

    String allowMultipleContexts() default "true";

    String sparkUser() default "spark";
}
