package com.liuyq.designpatter.observer.easy;

/**
 * Created by liuyq on 2017/12/8.
 * 这个是观察者 发现被观察者发生了变化，则做出相应的回应
 */
public interface Observer {
    void update(Observable observable);
}
