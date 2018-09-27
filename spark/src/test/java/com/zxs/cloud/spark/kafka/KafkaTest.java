package com.zxs.cloud.spark.kafka;

import com.zxs.cloud.spark.model.User;
import com.zxs.cloud.spark.publish.ZxsProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bill.zheng in 2018/9/4
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class KafkaTest {

    @Autowired
    private ZxsProducer zxsProducer;

    @Test
    public void testZxsProducer(){
        User user = new User();
        user.setAge(10);
        user.setGender("male");
        user.setId(3L);
        user.setUserName("测试");
        zxsProducer.send(user);
    }
}
