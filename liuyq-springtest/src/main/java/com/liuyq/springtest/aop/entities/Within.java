package com.liuyq.springtest.aop.entities;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by liuyq on 2019/4/21.
 */
@Aspect
@Component
@Order(0)
public class Within {

    //用了这个AnyJoinpontAnnotation注解的类全会被拦截
    @Before("@within(com.liuyq.springtest.aop.annotation.AnyJoinpontAnnotation)")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println(joinPoint.getTarget().getClass());
        System.out.println("Within AOP Before Advice...");  //(2)
    }
}
