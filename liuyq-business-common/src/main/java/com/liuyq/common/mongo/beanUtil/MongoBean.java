package com.liuyq.common.mongo.beanUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 创建人:LEN
 * 创建日期: 2017/06/08
 * 文件描述:
 */
public class MongoBean implements Cloneable {

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            MongoKit.INSTANCE.error("MongoBean.class", e.getMessage());
        }
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Map toMap() {
        return MongoKit.INSTANCE.toMap(this);
    }

    public JSONObject toJSONObject() {
        return (JSONObject) JSON.toJSON(this);
    }

    public String toJSONString() {
        return  JSON.toJSONString(this);
    }


}
