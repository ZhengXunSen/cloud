package com.zxs.cloud.spark.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bill.zheng in 2018/9/4
 */
@Component
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String brokers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String deserializer;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offset;

    public Map<String, Object> getKafkaParam(){
        Map<String, Object > kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("group.id", groupId);
        kafkaParams.put("key.deserializer", deserializer);
        kafkaParams.put("value.deserializer", deserializer);
        kafkaParams.put("auto.offset.reset", offset);
        kafkaParams.put("enable.auto.commit", false);
        return kafkaParams;
    }
}
