package com.zxs.cloud.spark.model;

import com.zxs.cloud.spark.model.base.SparkModel;
import lombok.Data;

/**
 * Created by bill.zheng in 2018/9/4
 */
@Data
public class User implements SparkModel {

    private Long id;

    private String userName;

    private int age;

    private String gender;
}
