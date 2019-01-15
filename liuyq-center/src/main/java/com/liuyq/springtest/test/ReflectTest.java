package com.liuyq.springtest.test;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;

/**
 * Created by liuyq on 2019/1/11.
 */
public class ReflectTest {
    @Test
    public void test() throws NoSuchMethodException {
        ReflectBo reflectBo = new ReflectBo();
        Constructor constructor = reflectBo.getClass().getDeclaredConstructor((Class[])null);
        Object o = BeanUtils.instantiateClass(constructor, new Object[0]);
    }
}
