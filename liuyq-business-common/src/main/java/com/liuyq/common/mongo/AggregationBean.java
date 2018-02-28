package com.liuyq.common.mongo;

import java.util.Map;

/**
 * Created by 10731 on 2017/12/20.
 */
public class AggregationBean {


    private Integer count;

    private Map<String, String> id;

    public Map<String, String> getId() {
        return id;
    }

    public void setId(Map<String, String> id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
