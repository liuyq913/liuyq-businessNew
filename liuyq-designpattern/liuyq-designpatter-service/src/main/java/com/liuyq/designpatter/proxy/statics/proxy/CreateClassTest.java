package com.liuyq.designpatter.proxy.statics.proxy;

import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by liuyq on 2017/12/6.
 */
public class CreateClassTest {
    //将生产的代理对象看是什么样子的
    //生成的代理类对每一个方法的处理就是回调invoke方法
    @Test
    public void testClass()throws Exception{
        //TestProxy:代理类的名称  Connection 接口名称
        byte[] bytes = ProxyGenerator.generateProxyClass("TestProxy", new Class[]{Connection.class});
        File file = new File("D:\\demo\\testProxy.class");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }
}


