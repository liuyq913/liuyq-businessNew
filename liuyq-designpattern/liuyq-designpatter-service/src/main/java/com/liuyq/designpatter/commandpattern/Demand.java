package com.liuyq.designpatter.commandpattern;

/**
 * Created by liuyq on 2017/12/26.
 * 具体的任务，需要关联一个开发者
 */
public class Demand implements Task{
    private Programmer programmer;

    public Demand(Programmer programmer){
        super();
        this.programmer = programmer;
    }
    @Override
    public void handle() {
        programmer.handleDemand();
    }

    public String toString() {
        return "Problem [programmer=" + programmer.getName() + "]";
    }
}
