package com.liuyq.designpatter.commandpattern;

/**
 * Created by liuyq on 2017/12/26.
 */
public class Problem implements Task{
    private Programmer programmer;

    public Problem(Programmer programmer){
        super();
        this.programmer = programmer;
    }
    @Override
    public void handle() {
        programmer.handleProblem();
    }
    public String toString() {
        return "Problem [programmer=" + programmer.getName() + "]";
    }
}
