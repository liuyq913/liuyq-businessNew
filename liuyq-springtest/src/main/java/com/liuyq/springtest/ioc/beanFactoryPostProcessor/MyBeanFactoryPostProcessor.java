package com.liuyq.springtest.ioc.beanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Created by liuyq on 2019/5/5.
 *
 * 再beanFactory容器初始化 之后，通过实现BeanFactoryPostProcessor(后置处理器) 接口来修改一下东西，
 * 真正调用的地方是再 invokeBeanFactoryPostProcessors
 *
 *  BeanFactoryPostProcessor
 */
@Component("myBeanFactoryPostProcessor")
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("调用MyBeanFactoryPostProcessor的postProcessBeanFactory");
        BeanDefinition bd = configurableListableBeanFactory.getBeanDefinition("myBeanFactoryPostProcessor");
        MutablePropertyValues pv =  bd.getPropertyValues();
        if (pv.contains("remark")) {
            pv.addPropertyValue("remark", "在BeanFactoryPostProcessor中修改之后的备忘信息");
        }
    }
}
