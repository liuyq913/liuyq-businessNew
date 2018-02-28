package com.liuyq.designpatter.factorymethod;

/**
 * Created by liuyq on 2017/12/8.
 */
public class LightAFactory implements producerInterface{
    @Override
    public Light creatLigh() {
        return new LightA();
    }
}
