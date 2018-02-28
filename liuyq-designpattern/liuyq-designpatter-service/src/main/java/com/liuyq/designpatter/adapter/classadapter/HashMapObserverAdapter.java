package com.liuyq.designpatter.adapter.classadapter;

import com.liuyq.designpatter.observer.advance.Observable;
import com.liuyq.designpatter.observer.advance.Observer;

import java.util.HashMap;

/**
 * Created by liuyq on 2017/12/15. *
 *
 *  配器模式从实现方式上分为两种，类适配器和对象适配器，这两种的区别在于实现方式上的不同，一种采用继承，一种采用组合的方式
 * 另外从使用目的上来说，也可以分为两种，特殊适配器和缺省适配器，这两种的区别在于使用目的上的不同，一种为了复用原有的代码并适配当前的接口，
 * 一种为了提供缺省的实现，避免子类需要实现不该实现的方法。
 *
 *适配器是用场景：场景通常情况下是，系统中有一套完整的类结构，而我们需要利用其中某一个类的功能（通俗点说可以说是方法），
 *             但是我们的客户端只认识另外一个和这个类结构不相关的接口，这时候就是适配器模式发挥的时候了，我们可以将这个
 *             现有的类与我们的目标接口进行适配，最终获得一个符合需要的接口并且包含待复用的类的功能的类。
 *
 */
//类适配器  一种采用继承
//比如我们希望将HashMap这个类加到观察者列表里，在被观察者产生变化时，假设我们要清空整个MAP。但是现在加不进去啊
public class HashMapObserverAdapter<K, V> extends HashMap<K, V> implements Observer{

    public void update(Observable o, Object arg) {
        //被观察者发生变化的时候，清空HashMap
        super.clear();
    }
}
