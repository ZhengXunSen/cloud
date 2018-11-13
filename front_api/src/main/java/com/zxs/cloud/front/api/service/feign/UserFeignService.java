package com.zxs.cloud.front.api.service.feign;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by bill.zheng in 2018/11/12
 */
@FeignClient(value = "user-service")
public interface UserFeignService {

    @RequestLine("GET /testConfig")
    String getUserName();
}
