package com.zxs.cloud.front.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zxs.cloud.front.api.service.feign.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bill.zheng in 2018/11/12
 */
@Service
public class UserService {

    @Autowired
    private UserFeignService userFeignService;

    /**
     * 使用HystrixCommand可以配置相应隔离线程池等参数，所以不适用feign直接fallback
     * @return
     */
    @HystrixCommand(groupKey = "userGroup", threadPoolKey = "userGroupPool",commandKey = "getUserName", fallbackMethod = "fallbackMethod",
    commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")},
    threadPoolProperties = {
           @HystrixProperty(name="coreSize", value = "30"),
           @HystrixProperty(name="maxQueueSize", value = "101"),
           @HystrixProperty(name="keepAliveTimeMinutes", value = "2"),
           @HystrixProperty(name="queueSizeRejectionThreshold", value = "15"),
           @HystrixProperty(name="metrics.rollingStats.numBuckets", value = "12"),
           @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value = "1440"),

    })
    public String getUserName(){
        return userFeignService.getUserName();
    }

    public String fallbackMethod() {
        return "返回默认用户名bill";
    }
}
