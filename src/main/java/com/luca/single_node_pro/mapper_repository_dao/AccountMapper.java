package com.luca.single_node_pro.mapper_repository_dao;

import com.luca.single_node_pro.domain_entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AccountMapper继承基类
 */
@Mapper
public interface AccountMapper extends MyBatisBaseDao<Account, Integer, AccountExample> {
    List<Account> findAll();
}