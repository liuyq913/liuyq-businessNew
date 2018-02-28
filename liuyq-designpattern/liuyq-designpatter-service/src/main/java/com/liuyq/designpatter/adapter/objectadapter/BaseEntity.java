package com.liuyq.designpatter.adapter.objectadapter;

/**
 * Created by liuyq on 2017/12/15.
 * 对象适配器:采用的是组合的方式
 *
 * 为什么要用对象适配器呢？？？
 * 因为Java的父类只能是一个，所以类适配器只能继承一个父类，
 * 所以当我们要适配的对象是两个类的时候，你怎么办呢？你难道要将两个类全部写到extends后面吗，
 * 如果你这么做了，那么编译器会表示它的不满的。
 */
public class BaseEntity {
}
