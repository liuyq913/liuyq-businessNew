package com.liuyq.springtest.aop.entities;

import com.liuyq.springtest.aop.annotation.UserLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyq on 2019/4/21.
 */
/*@Aspect
@Component
@Order(3)*/
public class UserLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserLogAspect.class);



    @Around("@annotation(userOptLog)")
    public Object doAround(final ProceedingJoinPoint call, UserLog userOptLog) throws Throwable {
       /* Object result = null;
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
        return result;*/
        Object[] args = call.getArgs();
        String requset = null;
        for(Object o : args){
            Class clazz = o.getClass();
            System.out.println("模拟记录日志"+clazz.getMethod("getRequestNo").invoke(o));

            /*if(o instanceof ModelSub){
                ModelSub modelSub = (ModelSub)o;
                requset = modelSub.getRequestNo();
            }*/
        }
        return call.proceed();
    }
}
