package com.liuyq.designpatter.decorator.first;

/**
 * Created by liuyq on 2017/12/18.
 * 待装饰的对象（原始对象）
 */
public class ConcreteComponent implements Component{
    @Override
    public void method() {
        System.out.println("原来的方法");
    }
}
