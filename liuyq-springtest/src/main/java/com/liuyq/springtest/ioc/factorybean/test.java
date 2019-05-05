package com.liuyq.springtest.ioc.factorybean;

import com.liuyq.springtest.ioc.applicationContext.event.MethodExeuctionEventPublisher;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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

        newsProvider.setPropertyValue("make","bus");
    }

    @Test
    public void test3(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        MethodExeuctionEventPublisher methodExeuctionEventPublisher = (MethodExeuctionEventPublisher)context.getBean("methodExeuctionEventPublisher");
        //methodExeuctionEventPublisher.methodToMonitor();
        System.out.println(methodExeuctionEventPublisher.hashCode());
        MethodExeuctionEventPublisher methodExeuctionEventPublisher2 = (MethodExeuctionEventPublisher)context.getBean("methodExeuctionEventPublisher");
        System.out.println(methodExeuctionEventPublisher2.hashCode());
    }

    @Test
    public void test4(){
        FileSystemXmlApplicationContext context =  new FileSystemXmlApplicationContext("classpath:applicationContext.xml");

        Person person = (Person) context.getBean("person");
        System.out.println(person);
    }
}
