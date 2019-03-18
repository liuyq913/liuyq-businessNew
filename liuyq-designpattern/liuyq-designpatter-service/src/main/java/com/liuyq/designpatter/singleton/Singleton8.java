package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 *  静态内部类
 *
 */
public class Singleton8 {

    private Singleton8(){}

    public static class Singleton9{
       private static final Singleton8 singleton1 = new Singleton8();
    }

    public static  Singleton8 getSingleton8(){
        return Singleton9.singleton1;
    }
}
