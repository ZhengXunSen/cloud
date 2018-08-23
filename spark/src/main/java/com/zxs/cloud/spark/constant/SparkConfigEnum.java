package com.zxs.cloud.spark.constant;

import lombok.Getter;

/**
 * Created by bill.zheng in 2018/8/22
 */
@Getter
public enum SparkConfigEnum {

    TRUE("true"),
    FALSE("false"),
    ;
    private String value;

    SparkConfigEnum(String value){
        this.value = value;
    }
}
