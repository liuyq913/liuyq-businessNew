package com.liuyq.designpatter.adapter.objectadapter;

/**
 * Created by liuyq on 2017/12/15.
 */
public class User extends BaseEntity{
    private Integer id;
    private String name;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
