package com.liuyq.solr.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liuyq on 2017/7/21.
 */
@SolrDocument(solrCoreName = "clues")
public class CluesDocument implements Serializable {
    private static final long serialVersionUID = 7051857581340590763L;

    @Field("id")
    private String id;

    /**
     * t_CustomerInfo
     */
    @Field("customerId")
    private Integer customerId;//客户信息表ID

    @Field("custId")
    private Integer custId;//所属车商ID

    @Field("customerType")
    private Integer customerType;//客户类型：0个人 1：同行

    @Field("mobile")
    private String mobile;//客户手机号

    @Field("isNew")
    private Boolean isNew;//是否新用户(true:是 false 否)

    @Field("lastcontactDate")
    private Date lastcontactDate;//最新联系时间

    @Field("userId")
    private Integer userId;//客户ID

    @Field("userName")
    private String userName;//客户姓名

    /**
     * t_ClusInfo
     */

    @Field("isContact")
    private Boolean isContact;//是否联系 : true:是 false:否

    @Field("clueId")
    private Integer clueId;//线索ID

    @Field("contactTime")
    private Date contactTime;//联系时间

    @Field("createTime")
    private Date createTime;//创建时间

    @Field("clusType")
    private Integer clusType;//0：通话记录 1：同行询价 2：砍价

    @Field("callState")
    private Boolean callState;//通话状态 (0 未接通 1已接通)

    @Field("source")
    private Integer source;//来源 0：ios 1:wap 2:web(待定)

    @Field("bizId")
    private Integer bizId;//线索类型为砍价，则为砍价表主键ID,如果为询价则为询价表主键ID，如果电话联系则为电话联系表主键ID

    @Field("carId")
    private Integer carId;//车辆ID

    @Field("brandName")
    private String brandName;//品牌名称

    @Field("modeName")
    private String modeName;//车型名称

    @Field("styleName")
    private String styleName;//车款名称

    @Field("operateType")
    private Integer operateType;//操作类型

    @Field("salePrice")
    private Double salePrice;//销售价格

    @Field("offerPrice")
    private Double offerPrice;//出价

    public CluesDocument() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Date getLastcontactDate() {
        return lastcontactDate;
    }

    public void setLastcontactDate(Date lastcontactDate) {
        this.lastcontactDate = lastcontactDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsContact() {
        return isContact;
    }

    public void setIsContact(Boolean isContact) {
        this.isContact = isContact;
    }

    public Integer getClueId() {
        return clueId;
    }

    public void setClueId(Integer clueId) {
        this.clueId = clueId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getContactTime() {
        return contactTime;
    }

    public void setContactTime(Date contactTime) {
        this.contactTime = contactTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getClusType() {
        return clusType;
    }

    public void setClusType(Integer clusType) {
        this.clusType = clusType;
    }

    public Boolean getCallState() {
        return callState;
    }

    public void setCallState(Boolean callState) {
        this.callState = callState;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

}
