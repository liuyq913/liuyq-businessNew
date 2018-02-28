package com.liuyq.designpatter.abstractfactory;

import org.junit.Test;

/**
 * Created by liuyq on 2017/12/8.
 */
public class ConcreteCreator1 implements Creator {
    @Override
    public ProductA CreatProductA() {
        return new ProductA1();
    }

    @Override
    public ProductB CreatProductB() {
        return new ProductB1();
    }

    @Test
    public void test(){
    }
}
