package com.liuyq.springtest.ioc.factorybean;

/**
 * Created by liuyq on 2019/3/26.
 */
public class Person {

    private  Car  car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public  String  toString(){

        return  car.getMake()+"::::"+car.getYear();
    }
}
