package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2017/12/4.
 * 最终版：类的静态属性会在类加载的时候就会初始化这是JVM帮我们保证的，
 * 所以我们无需担心并发访问的问题。所以在初始化进行一半的时候，别的线程是无法使用的，
 * 因为JVM会帮我们强行同步这个过程。另外由于静态变量只初始化一次，所以InnerClassSingleton仍然是单例的。
 */
public class InnerClassSingleton {
    private InnerClassSingleton(){}

    public static InnerClassSingleton getInstance(){
        return SingletonInstance.instance;
    }

    private static class SingletonInstance{

        static InnerClassSingleton instance = new InnerClassSingleton();

    }
}
