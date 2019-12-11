package com.infocity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author liaoqiangang
 * @date 2019/12/11 9:45 AM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@SpringBootApplication(scanBasePackages = "com.infocity")
@ComponentScan({"com.infocity.*","com.infocity.utils"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ShiroApp {

    public static void main(String[] args) {
        SpringApplication.run(ShiroApp.class, args);
    }

}

