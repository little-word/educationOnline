package com.guli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author GPX
 * @date 2019/12/11 16:20
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class StatisticsApplication {
    public static void main(String[] args){
     SpringApplication.run(StatisticsApplication.class,args);
    }
}
