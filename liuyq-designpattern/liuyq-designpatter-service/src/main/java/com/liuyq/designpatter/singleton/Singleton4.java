package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 * 懒汉式 <>缺点:效率低</>
 *
 */
public class Singleton4 {

    private static Singleton4 singleton1;

    private Singleton4(){}

    public static synchronized Singleton4 getSingleton1(){
        if(null == singleton1){
            singleton1 = new Singleton4();
        }
        return singleton1;
    }
}
