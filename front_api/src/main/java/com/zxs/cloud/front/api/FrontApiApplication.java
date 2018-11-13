package com.zxs.cloud.front.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableHystrix
@SpringBootApplication
@RefreshScope
public class FrontApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontApiApplication.class, args);
	}
}
