package com.liuyq.common.mongo.geospatial;


import com.liuyq.common.mongo.beanUtil.MongoBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Errol on 2017/7/28.
 */
public class TestBean extends MongoBean implements Serializable{

    private static final long serialVersionUID = 3050902737964210560L;
    private int mem;

    private Date date;
    private List<String> values;

    public TestBean(int i, Date date, List<String> values) {
        this.mem = i ;
        this.date = date;
        this.values = values;
    }

    public TestBean() {
    }


    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public int getMem() {
        return mem;
    }

    public void setMem(int mem) {
        this.mem = mem;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
