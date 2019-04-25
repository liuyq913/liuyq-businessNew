package com.liuyq.springtest.aop.demo.aoptest.proxy;

import com.liuyq.springtest.aop.demo.aoptest.service.SayService;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by liuyq on 2019/4/23.
 * cglib代理类   基于类的代理  代理对象相当于是目标对象的子类
 */
public class SayHelloCglibProxy implements MethodInterceptor {

    private Object target;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("基于cglib代理监听，开始...");
        Object result = method.invoke(target, objects); //调用目标对象的方法
        System.out.println("基于cglib代理监听，结束...");
        return result;
    }

    public Object getNewInstance(Object targetObject) {
        this.target = targetObject;
        Enhancer enhancer = new Enhancer();

        //设置父类
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this); //设置回掉方法

        return enhancer.create(); //创建代理对象
    }

    public static void main(String[] gars){
        SayHelloCglibProxy sayHelloCglibProxy = new SayHelloCglibProxy();
        SayService sayService = (SayService)sayHelloCglibProxy.getNewInstance(new SayService());

        sayService.sayHello("liuyuq");
    }
}
