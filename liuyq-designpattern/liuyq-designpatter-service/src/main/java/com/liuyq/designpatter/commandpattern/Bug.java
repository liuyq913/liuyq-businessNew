package com.liuyq.designpatter.commandpattern;

/**
 * Created by liuyq on 2017/12/26.
 */
public class Bug implements Task{
    private Programmer programmer;

    public Bug(Programmer programmer){
        super();
        this.programmer = programmer;
    }
    @Override
    public void handle() {
        programmer.handleBug();
    }

    public String toString() {
        return "Problem [programmer=" + programmer.getName() + "]";
    }
}
