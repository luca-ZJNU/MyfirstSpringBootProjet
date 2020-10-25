package com.luca.single_node_pro.controller;


import com.github.pagehelper.PageInfo;
import com.luca.single_node_pro.domain_entity.Account;
import com.luca.single_node_pro.domain_entity.Config;
import com.luca.single_node_pro.domain_entity.RespSta;
import com.luca.single_node_pro.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author luca
 * @create 2020-09-22 14:16
 *
 * 负责用户相关模块
 */
@Controller()
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountSrv;

    @Autowired
    Config config;

    @RequestMapping("/login")
    public String login(Model map){
        map.addAttribute("config",config);

        return "/account/login";
    }

    /*
    * 用户登陆异步校验
    * */
    @RequestMapping("/validataAccount")
    @ResponseBody
    public Object validataAccount(String loginName, String password, HttpServletRequest request){
        System.out.println("loginName:"+loginName+" password: "+password);

        Account account =  accountSrv.findByLoginNameAndPassword(loginName,password);
        if (account==null){
            return "登陆失败";
        }else{
            //如果登陆成功，我们将service返回的account对象写到session中，这样不同的controller和前端都能使用当前的登陆对象
            //比如用户要看自己的头像，账号密码，就不用每一次都要输入账号密码
            request.getSession().setAttribute("account",account);
            return "success";
        }
    }

    @RequestMapping("/logout")
    public String LogOut(HttpServletRequest request){

        request.getSession().removeAttribute("account");
        return "/index";
    }
    @RequestMapping("/list")
    public String list(Model map, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize ){
        PageInfo<Account> pageInfo =  accountSrv.findAll(pageNum,pageSize);
        map.addAttribute("pageInfo",pageInfo);
        return "/account/list";
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public RespSta deleteById(int id){

        System.out.println("s删除");
        return accountSrv.deleteById(id);
    }

    @RequestMapping("/register")
    public String register(){
        return "/account/register";
    }

    @RequestMapping("/validataRegister")
    @ResponseBody
    public RespSta ValidataRegister(String loginName, String password,String nikeName,String age,String location){
        RespSta register = accountSrv.register(loginName, password, nikeName, age, location);
        return register;
    }

    @RequestMapping("/profile")
    public String profile(Account account){
        System.out.println("进入profile函数");
        System.out.println(account);
        return "account/profile";
    }



    @RequestMapping("/valiprofile")
    public String valiprofile(Integer id,HttpServletRequest request,HttpServletResponse response,Model map){
        System.out.println("收到post请求");
        Account account =  accountSrv.findById(request,response,id);
       if (account == null ){
           return "你没有权限或没有找到用户数据";
       }else {
           System.out.println("应当跳转页面");
           map.addAttribute("account",account);
           return null;
       }
    }

   

}
