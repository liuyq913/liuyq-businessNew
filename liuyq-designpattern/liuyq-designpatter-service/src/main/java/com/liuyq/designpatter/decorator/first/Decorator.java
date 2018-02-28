package com.liuyq.designpatter.decorator.first;

/**
 * Created by liuyq on 2017/12/18.
 * 抽象装饰器父类，主要是为了我们需要装饰的目标是什么，并对
 * Component 进行了基础的装饰
 */
public abstract class Decorator implements Component{
    protected  Component component;

    public Decorator(Component component){
        super();
        this.component = component;
    }

    @Override
    public void method() {
        component.method();
    }
}
