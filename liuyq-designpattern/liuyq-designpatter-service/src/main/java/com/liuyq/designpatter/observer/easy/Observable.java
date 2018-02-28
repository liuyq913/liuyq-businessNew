package com.liuyq.designpatter.observer.easy;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liuyq on 2017/12/8.
 * 这个是被观察者，当自己发生变化的时候会通知所有观察他的人
 */
public class Observable {
    List<Observer> observerList = Lists.newArrayList();

    //添加观察者
    public void addObserver(Observer observer){
        observerList.add(observer);
    }
    //自己改变，通知所有观察者
    public void change(){
        System.out.println("被观察者发生了改变");
        notifyObservers(); //通知所有观察者
    }

    public void notifyObservers(){
        for(Observer observer : observerList){
            observer.update(this);
        }
    }

}
