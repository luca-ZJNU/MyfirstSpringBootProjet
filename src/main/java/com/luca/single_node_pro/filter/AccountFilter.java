package com.luca.single_node_pro.filter;


import com.luca.single_node_pro.domain_entity.Account;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author luca
 * @create 2020-09-24 16:33
 */
@Component
@WebFilter(urlPatterns = "/*") //什么样的url需要进入这个过滤器，所有的url都要走过滤器中过。
public class AccountFilter implements Filter { //要实现 javax.servlet下的Filter接口

    //不需要登陆的URI
    private final String[] IGNORED_URI = {"/index","/css","/js","/account/login","/images","/account/validataAccount","/account/register","/account/validataRegister"};

    //admin权限用户可访问的URI
    private final String[] ADMIN_ALLOWED = {"/account/list"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //filter启动之前回首先执行这个方法，这里可以写filter启动之前要加载的资源
        System.out.println("AccountFilter init");
    }

    //filter会将用户的请求拦截下来，以servletRequest参数传入该方法，同样，filter会将服务器的响应拦截下来，可以修改一些响应头等，以servletResponse参数传入该方法
    //filterChain：web服务器会创建一个代表Filter链的FilterChain对象传递给该方法。
    // 在doFilter方法中，开发人员如果调用了FilterChain对象的doFilter方法，则web服务器会检查FilterChain对象中是否还有filter，如果有，则调用第2个filter，如果没有，则调用目标资源。
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将ServletRequest类型强转为功能更强大的HttpServletRequest
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        //拿到当前请求的uri
        String uri = req.getRequestURI();

        //判断当前uri是否在ignore列表中
        if(isIgnored(uri)){
            filterChain.doFilter(req,res);
            return ;
        }
        //判断是否已经登陆

            /**从请求中获取session，看一下session里面是否有一个叫做account的东西，
            在AccountController里面我们是set了Account对象到session中，就叫做"account"
             */


        if (req.getSession().getAttribute("account") == null){
            //没有登陆，跳转到登陆页面,调用response的sendRedirect，将页面重定向到登陆页面
            res.sendRedirect("/account/login");

            return ; //这里应该return，就算重定向了，后面的语句依然会被执行

        }else if (isAdmin(uri)){//如果登陆成功，看看uri是否需要管理员权限

            Account sessionAccount = (Account)req.getSession().getAttribute("account");
            System.out.println(sessionAccount);

            if (sessionAccount.getRole().equals("admin") || sessionAccount.getRole().equals("superAdmin")){
                filterChain.doFilter(req,res);
                return ;
            }else {
                //res.sendRedirect("/index");

                actionWrite(res,"您没有该权限！");
                return ;
            }

        }else {//如果不需要管理员权限，直接放行
            filterChain.doFilter(req,res);
            return ;
        }

        //将这个请求和响应传给filter链中的下一个filter，意味着这一对请求和响应通过这一个filter，"放行！"

    }

    public void actionWrite(HttpServletResponse response,String data) {

        response.setContentType("text/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }


    private boolean isAdmin(String uri) {
        for (String val :ADMIN_ALLOWED){
            if (uri.startsWith(val)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIgnored(String uri) {
        //判断访问的uri起始部分是否包含IgnoredURI
        for (String val :IGNORED_URI){
            if (uri.startsWith(val)) {
                return true;
            }
        }
        return false;
    }


}
