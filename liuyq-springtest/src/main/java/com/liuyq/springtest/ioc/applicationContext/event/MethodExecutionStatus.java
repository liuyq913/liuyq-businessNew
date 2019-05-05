package com.liuyq.springtest.ioc.applicationContext.event;

/**
 * Created by liuyq on 2019/4/28.
 */
public enum MethodExecutionStatus {

    BEGIN("1","begin"),
    END("2","end");

    private String value;
    private  String content;

    MethodExecutionStatus(String value, String content) {
        this.value = value;
        this.content = content;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
