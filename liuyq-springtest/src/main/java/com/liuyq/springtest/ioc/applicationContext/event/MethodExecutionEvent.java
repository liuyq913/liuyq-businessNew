package com.liuyq.springtest.ioc.applicationContext.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by liuyq on 2019/4/28. 自定义事件
 *  aplication 继承了 ApplicationEventPublisher 来发布 ApplicationEvent（继承了EventObject）
 */
public class MethodExecutionEvent extends ApplicationEvent {

    private static final long serialVersionUID = 3068955179913528742L;
    private String methodName;
    private MethodExecutionStatus methodExecutionStatus;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MethodExecutionEvent(Object source) {
        super(source);
    }

    public MethodExecutionEvent(Object source,String methodName, MethodExecutionStatus methodExecutionStatus)
    {
        super(source);
        this.methodName = methodName;
        this.methodExecutionStatus = methodExecutionStatus;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodExecutionStatus getMethodExecutionStatus() {
        return methodExecutionStatus;
    }

    public void setMethodExecutionStatus(MethodExecutionStatus methodExecutionStatus) {
        this.methodExecutionStatus = methodExecutionStatus;
    }
}
