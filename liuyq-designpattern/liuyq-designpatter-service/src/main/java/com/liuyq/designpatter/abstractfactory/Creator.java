package com.liuyq.designpatter.abstractfactory;

/**
 * Created by liuyq on 2017/12/8.
 * 抽象工程模式
 *       定义：为创建一组相关或相互依赖的对象提供一个接口，而且无需指定他们的具体类。
        定义中说了，我们是要创建一个接口， 而这个接口是干嘛的呢，前面说了，是为了创建一组相关或者相互依赖的对象，而且还有一点就是，我们创建的对象不是具体的类，也就是说我们创建的是一个接口或者一个抽象类。

        工厂的接口里是一系列创造抽象产品的方法，而不再是一个，而相应的，抽象产品也不再是一个了，而是一系列相关的产品
 */
public interface Creator {

    ProductA CreatProductA();

    ProductB CreatProductB();
}