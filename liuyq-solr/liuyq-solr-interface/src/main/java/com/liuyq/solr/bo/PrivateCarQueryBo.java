package com.liuyq.solr.bo;

import java.io.Serializable;
import java.util.List;

public class PrivateCarQueryBo implements Serializable {
    private static final long serialVersionUID = -5578976238545909169L;

    private Integer saleType;

    private Integer brandID;

    private Integer messageType;

    private Integer custID;//好友车商ID

    private Integer currentUserID;//当前用户

    private Integer mainMemberID;//上级车商

    private String queryKey;

    private Integer modelID;

    private Integer cityID;

    private Integer provinceID;

    private List<Integer> focusList;

    public PrivateCarQueryBo(){};

    public PrivateCarQueryBo(Builder builder) {
        this.saleType = builder.saleType;
        this.brandID = builder.brandID;
        this.messageType = builder.messageType;
        this.custID = builder.custID;
        this.currentUserID = builder.currentUserID;
        this.mainMemberID = builder.mainMemberID;
        this.queryKey = builder.queryKey;
        this.modelID = builder.modelID;
        this.cityID = builder.cityID;
        this.provinceID = builder.provinceID;
        this.focusList = builder.focusList;
    }

    public static class Builder {
        private Integer saleType;

        private Integer brandID;

        private Integer messageType;

        private Integer custID;//好友车商ID

        private Integer currentUserID;//当前用户

        private Integer mainMemberID;//上级车商

        private String queryKey;

        private Integer modelID;

        private Integer cityID;

        private Integer provinceID;

        private List<Integer> focusList;

        public Builder buildeSaleType(Integer type) {
            this.saleType = type;
            return this;
        }

        public Builder buildeBrandID(Integer brandID) {
            this.brandID = brandID;
            return this;
        }

        public Builder builderMessageType(Integer messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder builderCustID(Integer custID) {
            this.custID = custID;
            return this;
        }

        public Builder builderCurrentUserID(Integer currentUserID) {
            this.currentUserID = currentUserID;
            return this;
        }
        public Builder builderMainMemberID(Integer mainMemberID) {
            this.mainMemberID = mainMemberID;
            return this;
        }

        public Builder builderQueryKey(String queryKey) {
            this.queryKey = queryKey;
            return this;
        }

        public Builder builderModelID(Integer modelID) {
            this.modelID = modelID;
            return this;
        }

        public Builder builderCityID(Integer cityID) {
            this.cityID = cityID;
            return this;
        }

        public Builder builderProvinceID(Integer provinceID) {
            this.provinceID = provinceID;
            return this;
        }

        public Builder builderFocusList(List<Integer> focusList) {
            this.focusList = focusList;
            return this;
        }

        public PrivateCarQueryBo create() {
            return new PrivateCarQueryBo(this);
        }
    }


    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public Integer getBrandID() {
        return brandID;
    }

    public void setBrandID(Integer brandID) {
        this.brandID = brandID;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public Integer getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(Integer currentUserID) {
        this.currentUserID = currentUserID;
    }

    public Integer getMainMemberID() {
        return mainMemberID;
    }

    public void setMainMemberID(Integer mainMemberID) {
        this.mainMemberID = mainMemberID;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public Integer getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(Integer provinceID) {
        this.provinceID = provinceID;
    }

    public List<Integer> getFocusList() {
        return focusList;
    }

    public void setFocusList(List<Integer> focusList) {
        this.focusList = focusList;
    }
}
