package com.liuyq.springtest.aop.entities;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

/**
 * Created by liuyq on 2019/4/21.
 */
/*@Aspect
@Component
@Order(0)*/
public class Within {

    //用了这个AnyJoinpontAnnotation注解的类全会被匹配
    @Before("@within(com.liuyq.springtest.aop.annotation.AnyJoinpontAnnotation)")
    //@Before("@target(com.liuyq.springtest.aop.annotation.AnyJoinpontAnnotation)")
    //@within 与 @target没有太大的区别，@within 属于静态匹配，@target
    public void doBefore(JoinPoint joinPoint) {
        System.out.println(joinPoint.getTarget().getClass());
        System.out.println("Within AOP Before Advice...");  //(2)
    }

}
