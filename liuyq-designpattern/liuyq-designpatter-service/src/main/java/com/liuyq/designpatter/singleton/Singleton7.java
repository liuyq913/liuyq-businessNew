package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2019/3/16.
 *  <>重检查[推荐用]  线程安全  </>
 *
 */
public class Singleton7 {

    private static Singleton7 singleton1;

    private Singleton7(){}

    public static Singleton7 getSingleton1(){
        if(null == singleton1){ //会产生多个事例
            synchronized (Singleton7.class) {
                if(null == singleton1) {
                    singleton1 = new Singleton7();
                }
            }
        }
        return singleton1;
    }
}
