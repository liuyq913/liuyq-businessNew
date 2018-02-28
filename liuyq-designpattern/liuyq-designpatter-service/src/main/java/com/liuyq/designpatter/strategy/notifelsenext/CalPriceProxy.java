package com.liuyq.designpatter.strategy.notifelsenext;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.SortedMap;

/**
 * Created by liuyq on 2017/12/15.
 */
public class CalPriceProxy implements InvocationHandler {

    private SortedMap<Integer, Class<? extends CalPrice>> clazzMap;

    public CalPriceProxy(SortedMap<Integer, Class<? extends CalPrice>> clazzMap){
        super();
        this.clazzMap = clazzMap;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Double result = 0D;
        if(method.getName().equals("calPrice")){
            for(Class<? extends CalPrice> clazz : clazzMap.values()){
                if(result == 0){
                    result = (Double) method.invoke(clazz.newInstance(),args);
                }else{
                    result = (Double) method.invoke(clazz.newInstance(),result);
                }
            }
            return result;
        }
        return null;
    }

    public static CalPrice getProxy(SortedMap<Integer, Class<? extends CalPrice>> clazzMap){
        return (CalPrice) Proxy.newProxyInstance(CalPriceProxy.class.getClassLoader(), new Class<?>[]{CalPrice.class}, new CalPriceProxy(clazzMap));
    }
}
