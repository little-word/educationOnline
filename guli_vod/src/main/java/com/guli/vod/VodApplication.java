package com.guli.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author GPX
 * @date 2019/12/16 17:54
 */
@SpringBootApplication
@EnableEurekaClient
public class VodApplication {
    public static void main(String[] args){
        SpringApplication.run(VodApplication.class,args);
    }
}
