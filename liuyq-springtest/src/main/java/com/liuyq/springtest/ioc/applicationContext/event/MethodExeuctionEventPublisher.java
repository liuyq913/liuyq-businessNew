package com.liuyq.springtest.ioc.applicationContext.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * Created by liuyq on 2019/4/28.
 */
@Component("methodExeuctionEventPublisher")
public class MethodExeuctionEventPublisher implements ApplicationEventPublisherAware{

    private ApplicationEventPublisher applicationEventPublisher;


    public void methodToMonitor() {
        MethodExecutionEvent methodExecutionEvent = new MethodExecutionEvent(this, "methodToMonitor", MethodExecutionStatus.BEGIN);
        this.applicationEventPublisher.publishEvent(methodExecutionEvent);

        MethodExecutionEvent methodExecutionEvent2 = new MethodExecutionEvent(this, "methodToMonitor", MethodExecutionStatus.END);
        this.applicationEventPublisher.publishEvent(methodExecutionEvent2);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}



