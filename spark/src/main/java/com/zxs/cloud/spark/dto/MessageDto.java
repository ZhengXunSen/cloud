package com.zxs.cloud.spark.dto;

import com.zxs.cloud.spark.model.base.SparkModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by bill.zheng in 2018/9/4
 */
@Data
public class MessageDto<T extends SparkModel> {

    private Long id;

    private T model;

    private Date sendTime;

    public MessageDto(T t){
        this.id = t.getId();
        this.model = t;
        this.sendTime = new Date();
    }
}
