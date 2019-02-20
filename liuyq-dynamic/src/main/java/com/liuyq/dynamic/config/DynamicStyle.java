package com.liuyq.dynamic.config;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lg on 2017/11/23.
 **/
public class DynamicStyle implements Serializable {
    private static final long serialVersionUID = 8289424930616717930L;

    private List<String> desc;

    private List<String> otherDesc;

    private List<String> title;

    private List<String> subTitle;

    private List<String> pic;

    private String clickurl;

    private String clickWapUrl;

    public DynamicStyle() {
    }

    public DynamicStyle(List<String> desc, List<String> otherDesc , List<String> title, List<String> subTitle, List<String> pic, String clickurl, String clickWapUrl) {
        this.desc = desc;
        this.otherDesc = otherDesc;
        this.title = title;
        this.subTitle = subTitle;
        this.pic = pic;
        this.clickurl = clickurl;
        this.clickWapUrl = clickWapUrl;
    }

    public DynamicStyle(DynamicStyle dynamicStyle) {
        this.desc = dynamicStyle.getDesc();
        this.otherDesc = dynamicStyle.getOtherDesc();
        this.title = dynamicStyle.getTitle();
        this.subTitle = dynamicStyle.getTitle();
        this.pic = dynamicStyle.getPic();
        this.clickurl = dynamicStyle.getClickurl();
        this.clickWapUrl = dynamicStyle.getClickWapUrl();
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }

    public List<String> getOtherDesc() {
        return otherDesc;
    }

    public void setOtherDesc(List<String> otherDesc) {
        this.otherDesc = otherDesc;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(List<String> subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public String getClickurl() {
        return clickurl;
    }

    public void setClickurl(String clickurl) {
        this.clickurl = clickurl;
    }

    public String getClickWapUrl() {
        return clickWapUrl;
    }

    public void setClickWapUrl(String clickWapUrl) {
        this.clickWapUrl = clickWapUrl;
    }
}
