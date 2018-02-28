package com.liuyq.designpatter.observer.easy;

/**
 * Created by liuyq on 2017/12/8.
 * 观察者实例
 */
public class Observer1 implements Observer {
    public void update(Observable observable) {
        System.out.println("观察者1号发现"+observable.getClass().getSimpleName());
    }
}
