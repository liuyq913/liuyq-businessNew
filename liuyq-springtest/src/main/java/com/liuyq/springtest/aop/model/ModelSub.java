package com.liuyq.springtest.aop.model;

/**
 * Created by liuyq on 2019/4/25.
 */
public class ModelSub extends Model1{
    private String requestNo;


    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    @Override
    public String toString() {
        return "ModelSub{" +
                "requestNo='" + requestNo + '\'' +
                '}';
    }
}
