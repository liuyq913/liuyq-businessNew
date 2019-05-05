package com.liuyq.springtest.ioc.applicationContext.event;


import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by liuyq on 2019/4/28.
 * 事件监听器
 */
@Component("methodExecutionEventListener")
public class MethodExecutionEventListener implements ApplicationListener<MethodExecutionEvent> {
    @Override
    public void onApplicationEvent(MethodExecutionEvent methodExecutionEvent) {
        System.out.println("【" + methodExecutionEvent.getMethodName() + "】" + "--" + methodExecutionEvent.getMethodExecutionStatus().getContent());

    }
}
