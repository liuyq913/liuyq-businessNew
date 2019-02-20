package com.liuyq.springtest.demo;

import com.liuyq.springtest.service.UserService;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuyq on 2019/2/20.
 */
public class test {
    @Test
    public void test1() throws Exception {
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        UserService userService = (UserService) resource.getBean("userService");
        userService.manyAdvices("1","2");
    }

    @Test
    public void test2(){
        String url = "[http://private4s.paodingcar.com:18080/download?fileID=8642033,http://private4s.paodingcar.com:18080/download?fileID=8642034,http://private4s.paodingcar.com:18080/download?fileID=8642035,http://private4s.paodingcar.com:18080/download?fileID=8642036,http://private4s.paodingcar.com:18080/download?fileID=8642037,http://private4s.paodingcar.com:18080/download?fileID=8642040,http://private4s.paodingcar.com:18080/download?fileID=8642041,http://private4s.paodingcar.com:18080/download?fileID=8642044,http://private4s.paodingcar.com:18080/download?fileID=8642046]";
        List list = Arrays.asList(url.replace("[","").replace("]","").split(","));
        System.out.println(list.toString());
        System.out.println(list.get(1)+","+list.get(2));
    }

    @Test
    public void test3(){
        String s = "{\"code\":\"0\",\"data\":{\"orderNo\":\"201901071604590000000794191745\",\"shareUrl\":\"http://www.99cherry.com/share/#/order-detail/201901071604590000000794191745\"}}";
        System.out.println(s.length());
    }
}
