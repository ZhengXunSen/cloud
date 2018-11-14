package com.zxs.cloud.front.api.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import feign.Contract;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by bill.zheng in 2018/11/12
 */
@Configuration
public class LoadBalanceConfig {

    @LoadBalanced//开启负载均衡支持
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder().build();
    }

    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

    /**
     * ribbon loadBalance algorithm
     * 可在此处注入自定义负载均衡算法
     * @return
     */
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }
}
