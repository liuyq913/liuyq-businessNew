package com.liuyq.springtest.tx;

import com.liuyq.springtest.tx.domain.TxExceptionDomain;
import com.liuyq.springtest.tx.model.TxException;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyq on 2019/2/20.
 */
public class test {
    @Test
    public void test1(){
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext(("classpath:applicationContext.xml"));
        TxExceptionDomain txExceptionDomain = (TxExceptionDomain)resource.getBean(TxExceptionDomain.class);
        TxException e = new TxException();
        e.setCreate_time(new Date());
        e.setGroup_id("1");
        txExceptionDomain.insert(e);
    }

    @Test
    public void test2(){
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("1123","232434345");
        nameMap.forEach(this::test);
    }

    public void test(String s , String s2){
        System.out.println(s + s2);
    }

}
