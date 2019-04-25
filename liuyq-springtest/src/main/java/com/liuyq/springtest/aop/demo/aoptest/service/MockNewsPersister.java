package com.liuyq.springtest.aop.demo.aoptest.service;

import com.liuyq.springtest.aop.demo.aoptest.Interface.IFXNewsPersister;
import com.liuyq.springtest.aop.model.FXNewsBean;

/**
 * Created by liuyq on 2019/4/25.
 */
public class MockNewsPersister implements IFXNewsPersister {
    private FXNewsBean newsBean;

    public void persistNews(FXNewsBean bean) {

        persistNews();
    }

    public void persistNews()

    {
        System.out.println("persist bean:" + getNewsBean());
    }

    public FXNewsBean getNewsBean() {
        return newsBean;
    }

    public void setNewsBean(FXNewsBean newsBean) {
        this.newsBean = newsBean;
    }
}
