package com.liuyq.solr.dmain;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.collect.Lists;
import com.liuyq.Pages.Page;
import com.liuyq.solr.bo.PrivateCarDocumentBo;
import com.liuyq.solr.bo.PrivateCarQueryBo;
import com.liuyq.solr.domain.PrivateCarSearchDomain;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by liuyq on 2017/9/26.
 */
public class PrivateCarSearchTest {

    public <T> T getService(Class<T> clazz) {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("PrivateCarSearchTest");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://192.168.100.131:2181");
        ReferenceConfig<T> reference = new ReferenceConfig<>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能�?�成内存和连接泄�?
        reference.setInterface(clazz);
        reference.setVersion("3.1.0.3");
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setGroup("liu");
        reference.setProtocol("dubbo");
        return reference.get();
    }
    @Test
    public void findPrivateCarDocumentByEentryID(){
        PrivateCarSearchDomain privateCarSearchDomain =  getService(PrivateCarSearchDomain.class);
        List<PrivateCarDocumentBo> privateCarDocumentBoList =  privateCarSearchDomain.findPrivateCarDocumentByEentryID(3102);
        System.out.println(privateCarDocumentBoList);
    }
    @Test
    public void findByKey(){
        PrivateCarSearchDomain privateCarSearchDomain =  getService(PrivateCarSearchDomain.class);
        List<Integer> list = Arrays.asList(5274);
        Pageable pageable = new PageRequest(0,10);
        Page<PrivateCarDocumentBo> page = privateCarSearchDomain.findByKey(5274, 1, list, "丰田", 10,0);
    }

    @Test
    public void search(){
        PrivateCarSearchDomain privateCarSearchDomain =  getService(PrivateCarSearchDomain.class);
        PrivateCarQueryBo privateCarQueryBo = new PrivateCarQueryBo();
        List<Integer> lsit = Lists.newArrayList();
        privateCarQueryBo.setFocusList(lsit);
        privateCarSearchDomain.search(privateCarQueryBo, 10, 0);
    }

    @Test
    public void test(){
        //System.out.println(24/5*2);
       // FileSystemXmlApplicationContext
        String shareKey = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(shareKey);
    }

    @Test
    public void tset(){
        String code = "notconcern";
        String eventCode = "push_reminder_c";
        int lastIndex = eventCode.lastIndexOf("_");
        String leftCode=eventCode.substring(0, lastIndex);
        String rightCode = eventCode.substring(lastIndex, eventCode.length());
        eventCode = leftCode+"_"+code+rightCode;
        String eventName = null;
        if(code.equals("update")){
            eventName = "提醒升级_周六15:00";
        }else if(code.equals("notconcern")){
            eventName = "提醒未关注商家_周六18:00";
        }
    }

    @Test
    public void test1(){
        Integer i = 1;
        Integer b = 1;
        System.out.println(i == b);
        System.out.println(i.equals(b));
    }
}
