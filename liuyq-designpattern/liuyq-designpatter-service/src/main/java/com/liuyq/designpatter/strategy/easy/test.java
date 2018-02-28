package com.liuyq.designpatter.strategy.easy;

/**
 * Created by liuyq on 2017/12/29.
 */
public class test {
    int cityID;
    int proviceId;


    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public test(int cityID, int proviceId) {
        this.cityID = cityID;
        this.proviceId = proviceId;
    }

    public int getProviceId() {
        return proviceId;
    }

    public void setProviceId(int proviceId) {
        this.proviceId = proviceId;
    }
}
