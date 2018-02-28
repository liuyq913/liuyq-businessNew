package com.liuyq.designpatter.strategy.easy;

/**
 * Created by liuyq on 2017/12/11.
 *
 * 真实的策略
 */
public class ConcreteStrategyA implements Strategy{

    public void algorithm() {
        System.out.println("采用策略A");
    }
}
