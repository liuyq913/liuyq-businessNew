package com.liuyq.designpatter.easyfactory;

import org.junit.Test;

/**
 * Created by liuyq on 2017/12/7.
 * 产品工程类
 */
public class ServletFactory {
    private ServletFactory(){}

    //典型的创造产品的方法，一般是静态的，因为工厂不需要有状态。
    public static Servlet createServlet(String servletName){
        if (servletName.equals("login")) {
            return new LoginServlet();
        }else if (servletName.equals("register")) {
            return new RegisterServlet();
        }else if (servletName.equals("loginout")) {
            return new LoginoutServlet();
        }else {
            throw new RuntimeException();
        }
    }

    @Test
    public void test(){
    }
}
