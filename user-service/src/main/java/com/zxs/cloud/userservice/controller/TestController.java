package com.zxs.cloud.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bill.zheng in 2018/11/10
 */
@RestController
@RefreshScope
public class TestController {

    @Value("${user.test.name}")
    private String userName;

    @RequestMapping("testConfig")
    public String testConfig(){
        return "配置文件读取的名字是：" + userName;
    }
}
