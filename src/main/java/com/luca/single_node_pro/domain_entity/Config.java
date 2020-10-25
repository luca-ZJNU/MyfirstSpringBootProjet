package com.luca.single_node_pro.domain_entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置相关
 * @author luca
 * @create 2020-10-09 10:10
 */
@Component
public class Config {
    //@Value(value = "XX石化") //使用Spring自动注入值
    @Value(value = "${Config.Name}") //从配置文件中读取Config.Name的值
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
