package com.guli.oss.util;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author GPX
 * @date 2019/12/9 13:40
 */
@Component
//@PropertySource("classpath:application.properties")

//InitializingBean接口为bean提供了初始化方法的方式，它只包括
//afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候都会执行该方法
public class ConstantPropertiesUtil implements InitializingBean {

    //映射application.properties 中的key
    //通过注解将常量、配置文件中的值、
    // 其他bean的属性值注入到变量中，作为变量的初始值
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.filehost}")
    private String fileHost;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    public static String FILE_HOST;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
        FILE_HOST = fileHost;
    }
}
