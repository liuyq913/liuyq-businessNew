package com.liuyq.designpatter.proxy.dynamic.proxy;

import com.liuyq.designpatter.proxy.statics.proxy.Connection;
import com.liuyq.designpatter.proxy.statics.proxy.DateSourse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liuyq on 2017/12/5.
 *
 * 动态代理：下面介绍下动态代理，动态代理是JDK自带的功能，
 * 它需要你去实现一个InvocationHandler接口，并且调用Proxy的静态方法去产生代理类。
 * */
public class ConnectionProxy implements InvocationHandler {

    private Connection connection;

    public ConnectionProxy(Connection connection) {
        super();
        this.connection = connection;
    }

    //这个方法是用来在生成的代理类用回调使用的
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里判断是Connection接口的close方法的话
        if(Connection.class.isAssignableFrom(proxy.getClass()) && method.getName().equals("close")){
            //我们不执行真正的close方法
            //method.invoke(connection, args);
            //将连接归还连接池
            DateSourse.getInstance().recoveryConnection(connection);
            return null;
        }else{
            return method.invoke(proxy, args);
        }
    }

    //生成动态代理类
    public Connection getConnectionProxy(){
        return (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Connection.class}, this);
    }
}
