package com.luca.single_node_pro.mapper_repository_dao;

import com.luca.single_node_pro.domain_entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
 * MenuMapper继承基类
 */
@Mapper
public interface MenuMapper extends MyBatisBaseDao<Menu, Integer, MenuExample> {
}