package com.liuyq.springtest.ioc.factorybean;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
}
