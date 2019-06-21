package com.liuyq.springtest.aop;

import com.liuyq.springtest.aop.demo.aoptest.Interface.ISay;
import com.liuyq.springtest.aop.demo.aoptest.proxy.SayHelloProxy;
import com.liuyq.springtest.aop.demo.aoptest.service.LogService;
import com.liuyq.springtest.aop.demo.aoptest.service.MockNewsPersister;
import com.liuyq.springtest.aop.demo.aoptest.service.SayService;
import com.liuyq.springtest.aop.model.Model1;
import com.liuyq.springtest.aop.model.ModelSub;
import com.liuyq.springtest.aop.service.AwareConfig;
import com.liuyq.springtest.aop.service.AwareService;
import com.liuyq.springtest.aop.service.FooService;
import com.liuyq.springtest.aop.service.UserService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.PatternMatchUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuyq on 2019/2/20.
 */
public class test {
    @Test
    public void test1() throws Exception {
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        UserService userService = (UserService) resource.getBean("userService");
        Model1 model1 = new Model1();
        model1.setId("1");
        model1.setName("liuyq");
        Model1 model2 = new Model1();
        model1.setId("2");
        model1.setName("liuyqqing");
        userService.manyAdvices(model1, model2);
    }

    @Test
    public void test2() {
        String url = "[http://private4s.paodingcar.com:18080/download?fileID=8642033,http://private4s.paodingcar.com:18080/download?fileID=8642034,http://private4s.paodingcar.com:18080/download?fileID=8642035,http://private4s.paodingcar.com:18080/download?fileID=8642036,http://private4s.paodingcar.com:18080/download?fileID=8642037,http://private4s.paodingcar.com:18080/download?fileID=8642040,http://private4s.paodingcar.com:18080/download?fileID=8642041,http://private4s.paodingcar.com:18080/download?fileID=8642044,http://private4s.paodingcar.com:18080/download?fileID=8642046]";
        List list = Arrays.asList(url.replace("[", "").replace("]", "").split(","));
        System.out.println(list.toString());
        System.out.println(list.get(1) + "," + list.get(2));
    }


    @Test
    public void test3() {
        ISay iHello = (ISay) new SayHelloProxy().bind(new SayService(), new LogService());
        iHello.sayHello("liuyq");
    }


    @Test
    public void test4() {
        String[] patterns = {"save*", "update*ser", "delete*"};
        boolean simpleMatch = PatternMatchUtils.simpleMatch(patterns, "updateUser");
        System.out.println(simpleMatch);

    }

    @Test
    public void testWithIn(){
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        FooService fooService = (FooService) resource.getBean("fooService");
        fooService.method1();
    }

    @Test
    public void testUserLog() throws Exception {
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        UserService userService = (UserService) resource.getBean("userService");
        ModelSub modelSub = new ModelSub();
        modelSub.setRequestNo("1223");
        userService.add(modelSub);
    }

    @Test
    public void test5(){
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext(("classpath:applicationContext.xml"));
        MockNewsPersister persister = (MockNewsPersister)resource.getBean("mockPersister");
        persister.persistNews();
        persister.persistNews();
    }

    @Test
    public void test6(){
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(AwareConfig.class);

        AwareService a  = context.getBean(AwareService.class);

        a.outPutResult();
    }

    @Test
    public void test7(){
        String s = "驾驶员座椅安全带扣:0,转向器:0,密封组件:0,成型密封件5:0,控制单元FEM1:0,修理套件驱动装置:0,前大灯（右）:0,副驾驶员座椅安全带:0,发动机总成:0,仪表台扬声器（右）:0,密封件组热交换器:0,控制单元REM2:0,控制单元REM1:0,副驾驶员座椅安全带扣:0,O形环17:0,密封件不含石棉:0,密封件7:0,涡轮增压器:0,仪表台扬声器（左）:0,进气歧管垫:0,排气歧管垫:0,驾驶员座椅安全带:0,油门踏板:0,后座椅安全带扣（右）:0,传感器轮2:0,后座椅安全带扣（左）:0,O形环43:0,空气滤清器芯:0";

        String[] s1 = s.split("，");
        for(int i = 0;i < s1.length; i++){
            System.out.println(s1[i]);
        }
    }
    @Test
    public void test8(){

    }
}
