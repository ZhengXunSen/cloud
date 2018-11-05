package com.zxs.cloud.fetch.config.datasource;

import com.zxs.cloud.fetch.constant.DataSource;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    DataSource value();
}
