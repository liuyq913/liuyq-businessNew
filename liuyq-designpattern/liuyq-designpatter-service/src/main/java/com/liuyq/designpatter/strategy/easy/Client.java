package com.liuyq.designpatter.strategy.easy;

/**
 * Created by liuyq on 2017/12/11.
 */
public class Client {
    public static void main(String[] args){
        Context context = new Context();
        context.setStrategy(new ConcreteStrategyA());
        context.mthod();

        context.setStrategy(new ConcreteStrategyB());
        context.mthod();
    }
}
