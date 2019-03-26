package com.liuyq.springtest.aop.demo.aoptest.service;

import com.liuyq.springtest.aop.demo.aoptest.Interface.ISay;

/**
 * Created by liuyq on 2019/2/21.
 */
public class SayService implements ISay {
    @Override
    public void sayHello(String name) {
        System.out.println("向"+name+"说：hello");
    }
}
