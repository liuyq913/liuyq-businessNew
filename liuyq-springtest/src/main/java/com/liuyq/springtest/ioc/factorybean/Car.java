package com.liuyq.springtest.ioc.factorybean;

/**
 * Created by liuyq on 2019/3/26.
 */
public class Car {

    private  String  make;
    private  int year;
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
