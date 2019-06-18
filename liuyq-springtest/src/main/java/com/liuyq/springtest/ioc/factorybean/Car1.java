package com.liuyq.springtest.ioc.factorybean;

/**
 * Created by liuyq on 2019/5/8.
 */
public class Car1 extends Car{

    public Car1(){
        System.out.println("子类构造");
    }


    public static void main(String[] args){
        new Car1();
    }
}
