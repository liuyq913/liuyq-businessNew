package com.liuyq.springtest.demo.aoptest.service;

import com.liuyq.springtest.demo.aoptest.Interface.ILog;

import java.lang.reflect.Method;

/**
 * Created by liuyq on 2019/2/21.
 */
public class LogService implements ILog{
    @Override
    public void start(Method method) {
        System.out.println(">>>>>调用目标方法（"+method.getName()+"）之前>>>>>>>");
    }

    @Override
    public void end(Method method) {
        System.out.println(">>>>>调用目标方法（"+method.getName()+"）后>>>>>>>");
    }
}
