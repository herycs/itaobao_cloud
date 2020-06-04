package com.itaobao.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @ClassName com.itaobao.file.FileApplication
 * @Description [文件操作类]
 * @Author ANGLE0
 * @Date 2020/6/4 14:17
 * @Version V1.0
 **/
@EnableEurekaClient
@SpringBootApplication
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
}
