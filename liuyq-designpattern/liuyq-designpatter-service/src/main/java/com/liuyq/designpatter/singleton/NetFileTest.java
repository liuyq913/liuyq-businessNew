package com.liuyq.designpatter.singleton;

import java.io.IOException;

/**
 * Created by liuyq on 2017/12/5.
 */
public class NetFileTest {
    public static void main(String[] args) throws IOException {
        Integer  i = 100;
        Integer i1 = 100;
        System.out.println(i == i1);
    }
}