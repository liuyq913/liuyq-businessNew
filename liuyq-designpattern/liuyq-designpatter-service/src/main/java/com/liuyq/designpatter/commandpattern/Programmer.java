package com.liuyq.designpatter.commandpattern;

/**
 * Created by liuyq on 2017/12/26.
 */
//程序员（完成任务）
public class Programmer {
    private String name;

    public Programmer(String name){
        super();
        this.name = name;
    }


    public void handleDemand(){
        System.out.println( name + "处理新需求");
    }

    public void handleBug(){
        System.out.println( name + "处理bug");
    }

    public void handleProblem(){
        System.out.println( name + "处理线上问题");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
