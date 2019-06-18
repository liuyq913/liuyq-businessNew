package com.liuyq.springtest.ioc.factorybean;

import com.liuyq.springtest.ioc.applicationContext.event.MethodExeuctionEventPublisher;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liuyq on 2019/3/26.
 */
public class test {
    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        Person person = (Person) context.getBean("person");
        System.out.println(person);
    }

    @Test
    public void test2() throws Exception {
       /* ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        Person person = (Person) context.getBean("person");
        System.out.println(person);*/


        Object car = Class.forName("com.liuyq.springtest.ioc.factorybean.Car").newInstance();

        BeanWrapper newsProvider = new BeanWrapperImpl(car);

        newsProvider.setPropertyValue("make", "bus");
    }

    @Test
    public void test3() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        MethodExeuctionEventPublisher methodExeuctionEventPublisher = (MethodExeuctionEventPublisher) context.getBean("methodExeuctionEventPublisher");
        //methodExeuctionEventPublisher.methodToMonitor();
        System.out.println(methodExeuctionEventPublisher.hashCode());
        MethodExeuctionEventPublisher methodExeuctionEventPublisher2 = (MethodExeuctionEventPublisher) context.getBean("methodExeuctionEventPublisher");
        System.out.println(methodExeuctionEventPublisher2.hashCode());
    }

    @Test
    public void test4() {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");

        Object o = context.getBean("myCarFactoryBean"); //获取到getObject返回的bean
        Object o1 = context.getBean("&myCarFactoryBean"); //获取倒FactoryBean的实现类
        if (o instanceof Car) {
            System.out.println("myCarFactoryBean 是Car ");
        } else {
            System.out.println("myCarFactoryBean 不是Car ");
        }

        if (o1 instanceof FactoryBean) {
            System.out.println("&myCarFactoryBean 是 FactoryBean ");
        }

    }

    @Test
    public void test5() {
        String responseContent = "success";
        if (!"success".equals(responseContent) && !"true".equals(responseContent)) {
            System.out.println(1);
        } else {
            System.out.println(2);
        }

        System.out.println(responseContent);
    }

    @Test
    public void test6() {
        String value = "1234";
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.reset();
        byte[] bytes;
        try {
            bytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.update(bytes);
        System.out.println(md5.digest());
    }
}
