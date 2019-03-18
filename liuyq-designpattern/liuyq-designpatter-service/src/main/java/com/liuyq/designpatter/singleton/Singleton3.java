package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 * 饿汉式 <>缺点:会产生多个事例</>
 *
 */
public class Singleton3 {

    private static Singleton3 singleton1;

    private Singleton3(){}

    public static Singleton3 getSingleton1(){
        if(null == singleton1){
            singleton1 = new Singleton3();
        }
        return singleton1;
    }
}
