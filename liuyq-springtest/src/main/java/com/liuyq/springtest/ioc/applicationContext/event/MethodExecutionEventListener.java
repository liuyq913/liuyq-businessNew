package com.liuyq.springtest.ioc.applicationContext.event;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by liuyq on 2019/4/28.
 * 事件监听器
 */
@Component("methodExecutionEventListener")
public class MethodExecutionEventListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof MethodExecutionEvent){
            MethodExecutionEvent event = (MethodExecutionEvent)applicationEvent;
            System.out.println("【"+event.getMethodName()+"】"+"--"+event.getMethodExecutionStatus().getContent());
        }
    }
}
