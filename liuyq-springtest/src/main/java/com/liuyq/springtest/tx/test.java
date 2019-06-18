package com.liuyq.springtest.tx;

import com.liuyq.springtest.tx.model.TxException;
import com.liuyq.springtest.tx.service.TxService;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Date;

/**
 * Created by liuyq on 2019/2/20.
 */
public class test {
    @Test
    public void test1(){
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext(("classpath:applicationContext.xml"));
        TxService persister = (TxService)resource.getBean("txService");
        TxException e = new TxException();
        e.setCreate_time(new Date());
        e.setGroup_id("1");
        persister.insert(e);
    }

}
