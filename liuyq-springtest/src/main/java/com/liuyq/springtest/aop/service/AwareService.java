package com.liuyq.springtest.aop.service;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


@Service
public class AwareService implements BeanNameAware, ResourceLoaderAware{

    private String beanName;

    private ResourceLoader resourceLoader;

    @Override
    public void setBeanName(String s) {
        this.beanName = s;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void outPutResult(){
        System.out.println("bean的名称"+beanName);

        Resource resource = resourceLoader.getResource("classpath:applicationContext.xml");

        try
        {
            System.out.println(resource.getInputStream());
        }catch (Exception e){}
    }
}
