package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 * 饿汉式 <>缺点：不是正规的饿汉式， 不需要的时候也会生成对象</>
 *
 */
public class Singleton2 {

    private static Singleton2 singleton1;

    private Singleton2(){}

    static {
        singleton1  = new Singleton2();
    }

    public static Singleton2 getSingleton1(){
        return singleton1;
    }
}
