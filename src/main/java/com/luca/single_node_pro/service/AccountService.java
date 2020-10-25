package com.luca.single_node_pro.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luca.single_node_pro.domain_entity.Account;
import com.luca.single_node_pro.domain_entity.RespSta;
import com.luca.single_node_pro.mapper_repository_dao.AccountExample;
import com.luca.single_node_pro.mapper_repository_dao.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.luca.single_node_pro.domain_entity.Account;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @author luca
 * @create 2020-09-22 14:16
 */
@Service
public class AccountService {

    @Autowired
    AccountMapper accountMapper;

    public Account findByLoginNameAndPassword(String loginName,String password){
        AccountExample example = new AccountExample();
        example.createCriteria()
                .andLoginNameEqualTo(loginName)
                .andPasswordEqualTo(password);
        List<Account> list = accountMapper.selectByExample(example);
        //查询可能的结果：1. 有一条数据，2. 没有数据
        // 3. 有多条数据，这个是不可能的，因为我们在设计数据库上 loginName就是unique的，所以不会出现这样的情况。所以数据库设计好可以省去很多麻烦

        return list.size() == 0 ? null: list.get(0);
    }

    public PageInfo<Account> findAll(int pageNum, int pageSize) {
        /**
         * PageHelper与PageInfo 都是com.github.pagehelper 包下的类
        PageHelper 的原理是在我们发的sql后面加了limit: 限制了查询的开始和结尾
        Preparing: select id, login_name, `password`, `role`, nike_name, age, `location` from account LIMIT ?, ?
         当我们调用PageHelper.startPage(pageNum,pageSize)，它就会影响后面的selectByExample()，这也是通过面向切面编程来实现的
       */
    /**
     * PageInfo 是pagehelper 提供的类，这个类主要来帮我们计算分页所需要的各种数据。
     * 在之前，我们要做分页都是手动写，先从数据库中将记录全部读出，看看有几行，然后根据页面的大小，计算出有几页，不组整页的还得+1
     * 还要算 前一页，后一页 等等。 现在框架为我们提供这样一个类，它会自动帮我们计算好我们需要的值。之后我们通过Controller将包含这些数据的对象传到前端
     * PageInfo<Acount> 是对我们向前端传的 List<Account>的一个封装，List<Account>被存在PageInfo的list属性下
     * */
        PageHelper.startPage(pageNum,pageSize);

        AccountExample example = new AccountExample();
        List<Account> list = accountMapper.selectByExample(example);
        //向PageInfo构造器中传入 我们从数据库查询的结果，第二个参数是：在HTML中要显示多少个翻页按钮，不加的话默认是8
        PageInfo<Account> pageInfo = new PageInfo<>(list, 9);
        System.out.println(pageInfo);
        return pageInfo;
    }


    public RespSta deleteById(int id) {
        int affectedRow = accountMapper.deleteByPrimaryKey(id);
        if (affectedRow == 1){
            return new RespSta(200,"","");
        }else {
            return new RespSta(500,"删除失败","");
        }
    }

    public RespSta register(String loginName, String password, String nikeName, String age, String location) {
        int ageint = Integer.parseInt(age);
        System.out.println("nikeName: " + nikeName+";"+"age: " + age+";"+"location: " + location);
        AccountExample example = new AccountExample();
        example.createCriteria().andLoginNameEqualTo(loginName);
        if (accountMapper.selectByExample(example).size() !=0){
            return new RespSta(500,"用户名已存在,注册失败!","");
        }else {
            Account account = new Account(loginName,password,nikeName,ageint,location);
            accountMapper.insert(account);
            return new RespSta(200,"注册成功","");
        }

    }

    public Account findById(HttpServletRequest request, HttpServletResponse response, Integer id) {
        Account account = (Account)request.getSession().getAttribute("account");
        String role = account.getRole();
        if (role.equals("admin")||role.equals("superAdmin")){
            System.out.println("权限满足！");
            try {
                System.out.println("accountMapper:应当跳转页面");
                response.sendRedirect("/account/profile");
            } catch (IOException e) {
                System.out.println("response.sendRedirect：IOException");
            }
            return  accountMapper.selectByPrimaryKey(id);
        }else {
            System.out.println("权限不满足");
            return null;
        }

    }
}
