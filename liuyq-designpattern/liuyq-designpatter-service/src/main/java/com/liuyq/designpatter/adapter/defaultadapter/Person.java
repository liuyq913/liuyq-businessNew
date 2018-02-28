package com.liuyq.designpatter.adapter.defaultadapter;

/**
 * Created by liuyq on 2017/12/15.
 * 适配器从功能上分为：缺省适配器 定制适配器
 *
 * 缺省适配器的使用场景：
 * 结果就是实现这个接口的子类，很可能出现很多方法是空着的情况，
 * 因为你的接口设计的过大，导致接口中原本不该出现的方法出现了，
 * 结果现在子类根本用不上这个方法，但由于JAVA语言规则的原因，
 * 实现一个接口必须实现它的全部方法，所以我们的子类不得不被迫写一堆空方法在那，只为了编译通过。
 */
public interface Person {
    void speak();

    void listen();

    void work();
}
