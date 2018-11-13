package com.zxs.cloud.front.api.controller;

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

    @GetMapping("get")
    public String getUserName(){
        return userService.getUserName();
    }
}
