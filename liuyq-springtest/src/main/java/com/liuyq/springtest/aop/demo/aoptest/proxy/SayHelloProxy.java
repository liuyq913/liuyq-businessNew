package com.liuyq.springtest.aop.demo.aoptest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liuyq on 2019/2/21.
 */
public class SayHelloProxy implements InvocationHandler {

    //目标对象
    private Object target;

    //调用对象
    private Object proxy;

    //生成代理对象
    public Object bind(Object target, Object proxy) {
        this.target = target;
        this.proxy = proxy;
        return Proxy.newProxyInstance(this.target.getClass().getClassLoader(), this.target.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        //反射得到操作者的实例
        Class clazz = this.proxy.getClass();
        //反射得到操作者的Start方法
        Method start = clazz.getDeclaredMethod("start", new Class[]{Method.class});
        //反射执行start方法
        start.invoke(this.proxy, new Object[]{method});
        //执行要处理对象的原本方法
        method.invoke(this.target, args);
        //反射得到操作者的end方法
        Method end = clazz.getDeclaredMethod("end", new Class[]{Method.class});
        //反射执行end方法
        end.invoke(this.proxy, new Object[]{method});
        return result;
    }
}
