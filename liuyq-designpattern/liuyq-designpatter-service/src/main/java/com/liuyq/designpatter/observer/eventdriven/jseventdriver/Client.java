package com.liuyq.designpatter.observer.eventdriven.jseventdriver;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 */
public class Client {
    public static void main(String[] arg){
        //客户访问这个jsp
        ButtonJsp jsp = new ButtonJsp();
        jsp.getButton().dblClick();
        jsp.getButton().mouseMove(10, 100);//移动到10，100
        jsp.getButton().mouseMove(15, 90);//又移动到15,90
        jsp.getButton().click();//接着客户点了提交
    }
}
