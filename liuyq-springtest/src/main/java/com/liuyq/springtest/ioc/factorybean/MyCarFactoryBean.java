package com.liuyq.springtest.ioc.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by liuyq on 2019/3/26.
 */
public class MyCarFactoryBean implements FactoryBean<Car> {

    private String make;

    private int year;

    public void setMake(String make) {
        this.make = make;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public Car getObject() throws Exception {

        Car car = new Car();

        if (year != 0) {
            car.setYear(this.year);
        }

        if ("make".equals(make)) {
            car.setMake("we are making  bla bla  bla");
        } else {
            car.setMake(this.make);
        }

        return car;
    }

    @Override
    public Class<?> getObjectType() {

        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
