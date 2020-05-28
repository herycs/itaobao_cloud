package com.itaobao.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @ClassNameArticleApplication
 * @Description
 * @Author ANGLE0
 * @Date2019/12/2 19:54
 * @Version V1.0
 **/
@SpringBootApplication
@EnableEurekaClient
public class ArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker(){
        return new IdWorker(1, 1);
    }

}
