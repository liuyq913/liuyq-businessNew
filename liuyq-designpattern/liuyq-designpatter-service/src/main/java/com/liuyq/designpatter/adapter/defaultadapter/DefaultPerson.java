package com.liuyq.designpatter.adapter.defaultadapter;

/**
 * Created by liuyq on 2017/12/15.
 */
//缺省适配器
/**
 * 我们创造一个Person接口的默认实现，它里面都是一些默认的方法，当然这里因为没什么可写的就空着了
 * 只要继承这个默认的适配器（DefaultPerson），然后覆盖掉LZ感兴趣的方法就行了，比如speak和listen，
 * 至于work，由于适配器帮我们提供了默认的实现，所以就不需要再写了。
 */
public class DefaultPerson implements Person{
    public void speak() {

    }

    public void listen() {

    }

    public void work() {

    }
}
