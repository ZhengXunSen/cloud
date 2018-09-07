package com.zxs.cloud.spark.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by bill.zheng in 2018/9/4
 */
@Data
public class MessageDto {

    private Long id;

    private String data;

    private Date sendTime;

    public static MessageDto buildMessage(Long id,String data){
        MessageDto messageDto = new MessageDto();
        messageDto.setId(id);
        messageDto.setSendTime(new Date());
        messageDto.setData(data);
        return messageDto;
    }
}
