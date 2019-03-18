package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 * 蓝函数 <>缺点:会产生多个事例</>
 *
 */
public class Singleton5 {

    private static Singleton5 singleton1;

    private Singleton5(){}

    public static Singleton5 getSingleton1(){
        if(null == singleton1){
            singleton1 = new Singleton5();
        }
        return singleton1;
    }
}
