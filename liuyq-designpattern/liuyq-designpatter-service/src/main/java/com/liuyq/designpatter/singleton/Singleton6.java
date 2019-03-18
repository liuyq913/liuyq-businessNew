package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 * 懒汉式 <>缺点:会产生多个事例 </>
 *
 */
public class Singleton6 {

    private static Singleton6 singleton1;

    private Singleton6(){}

    public static Singleton6 getSingleton1(){
        if(null == singleton1){ //会产生多个事例
            synchronized (Singleton6.class) {
                singleton1 = new Singleton6();
            }
        }
        return singleton1;
    }
}
