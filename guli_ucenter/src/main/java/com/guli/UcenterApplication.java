package com.guli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author GPX
 * @date 2019/12/11 11:45
 */
@SpringBootApplication
@EnableEurekaClient
public class UcenterApplication {
    public static void main(String[] args){
        SpringApplication.run(UcenterApplication.class,args);
    }
}
