package com.liuyq.springtest.test;

import java.io.Serializable;

/**
 * Created by liuyq on 2019/1/11.
 */
public class ReflectBo implements Serializable{

    Object object = new Object();

    private String name;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
