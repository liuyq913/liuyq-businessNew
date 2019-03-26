package com.liuyq.springtest.aop.entities;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by liuyq on 2019/2/19.
 */
//切面类  优先级
@Component
@Aspect
public class Operator {

    //第一个* 表示返回值，（*）表示所有的类型，第二个* 表示所有的类，*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数。
    @Pointcut("execution(* com.liuyq.springtest.service..*.*(..))")

    public void pointCut(){}

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println(joinPoint.getTarget().getClass());
        System.out.println("AOP Before Advice...");  //(2)
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint){
        System.out.println("AOP After Advice..."); //(5)
    }

    @AfterReturning(pointcut="pointCut()",returning="returnVal")
    public void afterReturn(JoinPoint joinPoint, Object returnVal){ //(6)
        System.out.println("@AfterReturning：模拟日志记录功能...");
        System.out.println("@AfterReturning：目标方法包为：" +
                joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("@AfterReturning：目标方法为："+joinPoint.getSignature().getName());
        System.out.println("@AfterReturning：参数为：" +
                Arrays.toString(joinPoint.getArgs()));
        System.out.println("@AfterReturning：返回值为：" + returnVal);
        System.out.println("@AfterReturning：被织入的目标对象为：" + joinPoint.getTarget());
    }

    @AfterThrowing(pointcut="pointCut()",throwing="error")
    public void afterThrowing(JoinPoint joinPoint,Throwable error){ //异常抛出
        System.out.println("AOP AfterThrowing Advice..." + error);
        System.out.println("AfterThrowing...");
    }

    @Around("pointCut()")
    public Object  around(ProceedingJoinPoint pjp){
        System.out.println("AOP Aronud before..."); //（1）
        Object object = null;
        try {
            System.out.println(pjp.getArgs());
            object = pjp.proceed(); //（3）
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("AOP Aronud after..."); //(4)
        return "原来的值是这样的"+object+"修改之后的值是："+object+"ing"; //修改返回值 在AfterReturning的返回值里面会返回出来
    }

}
