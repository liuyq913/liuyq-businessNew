package com.liuyq.solr.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SolrDocument(solrCoreName = "privateCarSearch")
public class PrivateCarDocument implements Serializable {

    private static final long serialVersionUID = -4488474322047377221L;
    @Field("id")
    private String id;

    @Field("carId")
    private Integer carId;//车辆Id

    @Field("custId")
    private Integer custId;//车辆Id

    @Field("brandId")
    private Integer brandId;//品牌Id

    @Field("brandName")
    private String brandName;//品牌名称

    @Field("modelsName")
    private String modelsName;//车型名称

    @Field("styleName")
    private String styleName;//车款名称

    @Field("modelId")
    private Integer modelId;//车型Id

    @Field("alias")
    private String alias;//车型别名(厂商+车型)

    @Field("firstReg")
    private Date firstReg;//初登日期

    @Field("sellPrice")
    private Double sellPrice;//售价

    @Field("mile")
    private Integer mile;//里程

    @Field("provinceId")
    private Integer provinceId;//省份Id

    @Field("cityId")
    private Integer cityId;//城市Id

    @Field("colorId")
    private Integer colorId;//车辆颜色Id

    @Field("carAge")
    private Double carAge;//车龄

    @Field("carModel")
    private Integer carModel;//车辆类型:SUV,小车等

    @Field("carPhoto")
    private String carPhoto;//车辆封面照url

    @Field("emissionStandard")
    private String emissionStandard;//排放标准

    @Field("saleStatus")
    private Integer saleStatus;//销售状态

    @Field("memberType")
    private Integer memberType;//账户类型

    @Field("brandNameFacetValue")
    private String brandNameFacetValue;//车辆品牌信息

    @Field("modelNameFacetValue")
    private String modelNameFacetValue;//车辆车型信息

    @Field("name")
    private List<String> name;//用于搜索查询

    @Field("addTime")
    private Date addTime;//车辆发车时间

    @Field("facetValue")
    private List<String> facetValue;//用于搜索查询结果分组的

    @Field("modelPinyin")
    private String modelPinyin;//车系拼音缩写

    @Field("brandPinyin")
    private String brandPinyin;//品牌拼音缩写

    @Field("provincePinyin")
    private String provincePinyin;//省份拼音缩写

    @Field("cityPinyin")
    private String cityPinyin;//城市拼音缩写

    @Field("newCarPrice")
    private Double newCarPrice;//新车价格

    @Field("provinceName")
    private String provinceName;//省份名称

    @Field("cityName")
    private String cityName;//城市名称

    @Field("carSourceNo")
    private String carSourceNo;//车源编码

    @Field("styleId")
    private Integer styleId;//车款ID

    @Field("specialValue")
    private Integer specialValue;//特色主题排序用

    @Field("carType")
    private Integer carType;

    @Field("basicPrice")
    private Double basicPrice;

    @Field("isBrokers")
    private Integer isBrokers;

    @Field("isWholesale")
    private Integer isWholesale;

    @Field("operatePort")
    private Integer operatePort;

    @Field("saleManID")
    private Integer saleManID;

    @Field("saleManName")
    private String saleManName;

    @Field("carDescResult")
    private String carDescResult;

    @Field("commission")
    private Double commission;

    @Field("enterID")
    private Integer enterID;

    @Field("saleManMobile")
    private String saleManMobile;

    @Field("hasPicture")
    private Boolean hasPicture;

    @Field("hasVoice")
    private Boolean hasVoice;

    @Field("voiceUrl")
    private String voiceUrl;

    @Field("photos")
    private String photos;

    @Field("wholeSalePrice")
    private Double wholeSalePrice;

    @Field("shelfReason")
    private String shelfReason;

    @Field("storeName")
    private String storeName;

    @Field("sellCarID")
    private Integer sellCarID;

    @Field("pinyin")
    private String pinyin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelsName() {
        return modelsName;
    }

    public void setModelsName(String modelsName) {
        this.modelsName = modelsName;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getFirstReg() {
        return firstReg;
    }

    public void setFirstReg(Date firstReg) {
        this.firstReg = firstReg;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getMile() {
        return mile;
    }

    public void setMile(Integer mile) {
        this.mile = mile;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
    }

    public Double getCarAge() {
        return carAge;
    }

    public void setCarAge(Double carAge) {
        this.carAge = carAge;
    }

    public Integer getCarModel() {
        return carModel;
    }

    public void setCarModel(Integer carModel) {
        this.carModel = carModel;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getEmissionStandard() {
        return emissionStandard;
    }

    public void setEmissionStandard(String emissionStandard) {
        this.emissionStandard = emissionStandard;
    }

    public Integer getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Integer saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getBrandNameFacetValue() {
        return brandNameFacetValue;
    }

    public void setBrandNameFacetValue(String brandNameFacetValue) {
        this.brandNameFacetValue = brandNameFacetValue;
    }

    public String getModelNameFacetValue() {
        return modelNameFacetValue;
    }

    public void setModelNameFacetValue(String modelNameFacetValue) {
        this.modelNameFacetValue = modelNameFacetValue;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<String> getFacetValue() {
        return facetValue;
    }

    public void setFacetValue(List<String> facetValue) {
        this.facetValue = facetValue;
    }

    public String getModelPinyin() {
        return modelPinyin;
    }

    public void setModelPinyin(String modelPinyin) {
        this.modelPinyin = modelPinyin;
    }

    public String getBrandPinyin() {
        return brandPinyin;
    }

    public void setBrandPinyin(String brandPinyin) {
        this.brandPinyin = brandPinyin;
    }

    public String getProvincePinyin() {
        return provincePinyin;
    }

    public void setProvincePinyin(String provincePinyin) {
        this.provincePinyin = provincePinyin;
    }

    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }

    public Double getNewCarPrice() {
        return newCarPrice;
    }

    public void setNewCarPrice(Double newCarPrice) {
        this.newCarPrice = newCarPrice;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCarSourceNo() {
        return carSourceNo;
    }

    public void setCarSourceNo(String carSourceNo) {
        this.carSourceNo = carSourceNo;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Integer getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(Integer specialValue) {
        this.specialValue = specialValue;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public Double getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(Double basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Integer getIsBrokers() {
        return isBrokers;
    }

    public void setIsBrokers(Integer isBrokers) {
        this.isBrokers = isBrokers;
    }

    public Integer getIsWholesale() {
        return isWholesale;
    }

    public void setIsWholesale(Integer isWholesale) {
        this.isWholesale = isWholesale;
    }

    public Integer getOperatePort() {
        return operatePort;
    }

    public void setOperatePort(Integer operatePort) {
        this.operatePort = operatePort;
    }

    public Integer getSaleManID() {
        return saleManID;
    }

    public void setSaleManID(Integer saleManID) {
        this.saleManID = saleManID;
    }

    public String getSaleManName() {
        return saleManName;
    }

    public void setSaleManName(String saleManName) {
        this.saleManName = saleManName;
    }

    public String getCarDescResult() {
        return carDescResult;
    }

    public void setCarDescResult(String carDescResult) {
        this.carDescResult = carDescResult;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Integer getEnterID() {
        return enterID;
    }

    public void setEnterID(Integer enterID) {
        this.enterID = enterID;
    }

    public String getSaleManMobile() {
        return saleManMobile;
    }

    public void setSaleManMobile(String saleManMobile) {
        this.saleManMobile = saleManMobile;
    }

    public Boolean getHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(Boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public Boolean getHasVoice() {
        return hasVoice;
    }

    public void setHasVoice(Boolean hasVoice) {
        this.hasVoice = hasVoice;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Double getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(Double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public String getShelfReason() {
        return shelfReason;
    }

    public void setShelfReason(String shelfReason) {
        this.shelfReason = shelfReason;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getSellCarID() {
        return sellCarID;
    }

    public void setSellCarID(Integer sellCarID) {
        this.sellCarID = sellCarID;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
