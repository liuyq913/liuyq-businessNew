package com.liuyq.designpatter.observer.advance;


/**
 * Created by Administrator on 2017-12-09.
 * 观察者接口，每个观察者都必须实现这个接口
 */
public interface Observer {
    //这个方法是观察者在观察对象产生变化时所做的响应动作，从中传入了观察的对象和一个预留参数
    void update(Observable o, Object arg);
}
