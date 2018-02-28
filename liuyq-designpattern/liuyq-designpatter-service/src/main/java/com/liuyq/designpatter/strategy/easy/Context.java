package com.liuyq.designpatter.strategy.easy;

/**
 * Created by liuyq on 2017/12/11.
 *
 * 上下文，拥有策略接口
 */
public class Context {
    private Strategy strategy;

    public void mthod(){
        strategy.algorithm();
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
