package uyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by lihonghua on 2019/2/14.
 * 服务监控地址
 * http://localhost:8099/actuator/health
 * 服务注册查看
 * http://localhost:8099/
 *
 * 注意：早期的版本（Dalston及更早版本）还需在启动类上添加注解 @EnableDiscoveryClient或 @EnableEurekaClient ，从Edgware开始，该注解可省略。
 */
@EnableCaching //开启缓存
@EnableScheduling //开启任务调度
@SpringBootApplication
@EnableEurekaServer
public class Startup {

    public static void main(String args[]){
//        EventConsumer eventConsumer = (EventConsumer) SpringUtil.getBean("eventConsumer");

        SpringApplication.run(Startup.class,args);
    }
}
