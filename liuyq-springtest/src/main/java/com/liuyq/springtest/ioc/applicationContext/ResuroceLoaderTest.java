package com.liuyq.springtest.ioc.applicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Created by liuyq on 2019/4/28.
 */
public class ResuroceLoaderTest {

    ResourceLoader resourceLoader = new ClassPathXmlApplicationContext("配置文件路径");
// 或者配置文件路径");  // ResourceLoader resourceLoader = new FileSystemXmlApplicationContext("
    Resource fileResource = resourceLoader.getResource("D:/spring21site/README");
    //assertTrue(fileResource instanceof ClassPathResource);
    //assertFalse(fileResource.exists());
    Resource urlResource2 = resourceLoader.getResource("http://www.spring21.cn");
   // assertTrue(urlResource2 instanceof UrlResource);
}
