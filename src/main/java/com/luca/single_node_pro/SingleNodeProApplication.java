package com.luca.single_node_pro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.luca.single_node_pro.mapper_repository_dao")
public class SingleNodeProApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleNodeProApplication.class, args);
    }

}