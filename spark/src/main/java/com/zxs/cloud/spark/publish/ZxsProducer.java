package com.zxs.cloud.spark.publish;

import com.zxs.cloud.spark.dto.MessageDto;
import com.zxs.cloud.spark.model.base.SparkModel;
import com.zxs.cloud.spark.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by bill.zheng in 2018/9/4
 */
@Slf4j
@Component
public class ZxsProducer {

    @Value("${spring.kafka.test.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    //发送消息方法
    public void send(SparkModel sparkModel) {
        MessageDto messageDto = MessageDto.buildMessage(sparkModel.getId(), JsonUtils.json(sparkModel));
        log.info("message = {}", JsonUtils.json(messageDto));
        kafkaTemplate.send(topic,  JsonUtils.json(messageDto));
    }
}
