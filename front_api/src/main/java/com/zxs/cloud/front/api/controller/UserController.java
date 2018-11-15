package com.zxs.cloud.front.api.controller;

import brave.Span;
import brave.Tracer;
import com.zxs.cloud.front.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bill.zheng in 2018/11/12
 */
@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private Tracer tracer;

    @GetMapping("get")
    public String getUserName(){
        // 创建一个 span
        final Span span = tracer.newTrace().name("3rd_service");
        try {
            span.tag(Span.Kind.CLIENT.name(), "3rd_service");
            span.annotate(Span.Kind.CONSUMER.name());
            // 这里时调用第三方 API 的代码
            return userService.getUserName();
        } finally {
            span.tag(Span.Kind.CLIENT.name(), "exception");
            span.annotate(Span.Kind.CONSUMER.name());
        }
    }
}
