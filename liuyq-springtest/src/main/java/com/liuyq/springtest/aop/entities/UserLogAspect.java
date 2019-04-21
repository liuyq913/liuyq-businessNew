package com.liuyq.springtest.aop.entities;

import com.liuyq.springtest.aop.annotation.UserLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by liuyq on 2019/4/21.
 */
@Aspect
@Component
@Order(3)
public class UserLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserLogAspect.class);



    @Around("@annotation(userOptLog)")
    public Object doAround(final ProceedingJoinPoint call, UserLog userOptLog) throws Throwable {
        Object result = null;
        Signature signature = call.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String desc = userOptLog.description();
        logger.info("调用方法时记录日志开始。。。。。。。");
        logger.info("方法："+method.getName()+"描述："+desc);
        try{
            result = call.proceed();//执行目标方法;
        }catch(Exception e) {
            logger.info("方法：" + method.getName() + "描述：" + desc + "目标方法执行失败。。。原因：" + e.getMessage());
        }
        return result;
    }
}
