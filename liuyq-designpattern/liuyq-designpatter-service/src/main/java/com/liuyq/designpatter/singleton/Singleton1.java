package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 * 饿汉式 <>缺点：不是正规的饿汉式， 不需要的时候也会生成对象</>
 *
 */
public class Singleton1 {

    private static Singleton1 singleton1  = new Singleton1();

    private Singleton1(){}

    public static Singleton1 getSingleton1(){
        return singleton1;
    }
}
