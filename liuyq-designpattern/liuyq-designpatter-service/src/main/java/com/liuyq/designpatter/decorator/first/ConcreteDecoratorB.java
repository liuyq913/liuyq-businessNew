package com.liuyq.designpatter.decorator.first;

/**
 * Created by liuyq on 2017/12/18.
 * 具体的装饰器
 */
public class ConcreteDecoratorB extends Decorator{

    public ConcreteDecoratorB(Component component) {
        super(component);
    }

    public void methodA(){
        System.out.println("被装饰器B扩展的功能");
    }

    public void method(){
        System.out.println("针对该方法加一层B包装");
        super.method();
        System.out.println("B包装结束");
    }
}
