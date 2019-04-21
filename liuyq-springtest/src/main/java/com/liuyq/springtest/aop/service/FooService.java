package com.liuyq.springtest.aop.service;

import com.liuyq.springtest.aop.annotation.AnyJoinpontAnnotation;
import org.springframework.stereotype.Service;

/**
 * Created by liuyq on 2019/4/21.
 */
@AnyJoinpontAnnotation
@Service("fooService")
public class FooService {

    public void method1(){
        System.out.println("method1 方法");
    }
    public void method2(){
        System.out.println("method2 方法");
    }

}
