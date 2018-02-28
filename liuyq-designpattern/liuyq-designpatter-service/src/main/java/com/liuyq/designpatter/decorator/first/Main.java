package com.liuyq.designpatter.decorator.first;

/**
 * Created by liuyq on 2017/12/21.
 */
public class Main {
    //个人理解：装饰器是在原有的方法上再增加自己的东西，不修改原有的方法
    public static void main(String[] agrs){
        Component component = new ConcreteComponent();//待装饰的对象
        component.method();//装饰之前的方法
        System.out.println("------------------------------------");
        ConcreteDecoratorA concreteDecoratorA = new ConcreteDecoratorA(component); //装饰成A
        concreteDecoratorA.method(); //原来的方法
        concreteDecoratorA.methodA(); //装饰器A特有的方法
        System.out.println("------------------------------------");
        ConcreteDecoratorB concreteDecoratorB = new ConcreteDecoratorB(component); //装饰成B
        concreteDecoratorB.method(); //原来的方法
        concreteDecoratorB.methodA(); //装饰器A特有的方法
        System.out.println("------------------------------------");
        ConcreteDecoratorB concreteDecoratorB2 = new ConcreteDecoratorB(concreteDecoratorA); //将待装饰对象装饰成A再装饰成B
        concreteDecoratorB2.method(); //原来的方法
        concreteDecoratorB2.methodA(); //装饰器A特有的方法
    }
}
