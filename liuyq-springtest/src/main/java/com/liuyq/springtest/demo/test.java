package com.liuyq.springtest.demo;

import com.liuyq.springtest.demo.aoptest.Interface.ISay;
import com.liuyq.springtest.demo.aoptest.proxy.SayHelloProxy;
import com.liuyq.springtest.demo.aoptest.service.LogService;
import com.liuyq.springtest.demo.aoptest.service.SayService;
import com.liuyq.springtest.service.Model1;
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
        Model1 model1 = new Model1();
        model1.setId("1");
        model1.setName("liuyq");
        Model1 model2 = new Model1();
        model1.setId("2");
        model1.setName("liuyqqing");
        userService.manyAdvices(model1, model2);
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
        ISay iHello = (ISay) new SayHelloProxy().bind(new SayService(),new LogService());
        iHello.sayHello("liuyq");
    }
}
