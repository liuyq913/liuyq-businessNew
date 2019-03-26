package com.liuyq.springtest.aop.demo.aoptest.Interface;

import java.lang.reflect.Method;

/**
 * Created by liuyq on 2019/2/21.
 */
public interface ILog {

    public void start(Method method);

    public void end(Method method);
}
