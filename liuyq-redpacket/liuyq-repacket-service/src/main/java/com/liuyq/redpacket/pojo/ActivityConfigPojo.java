package com.liuyq.redpacket.pojo;


import com.liuyq.redpacket.bo.RedPacketDistributionBo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 10731 on 2017/6/28.
 */
public class ActivityConfigPojo implements Serializable{

    private static final long serialVersionUID = 6066770770629760313L;

    private Integer ID;

    private Integer activityID;

    private Integer activityIssue;

    private Double redPacketDownRate;

    private Integer redPacketNum;

    private Integer prizeRedPacketNum;

    private Date startTime;

    private Date stopTime;

    private Integer allowCustNum;

    private Integer allowJoinNum;

    private Integer custAllowJoinNum;

    private Integer status;

    private Date createTime;

    private Boolean isDelete;

    private Integer custAllowWinningNum;

    private Integer timeInterval;

    private Double probability;

    private String name;

    //-------附加属性

    private List<RedPacketDistributionBo> distributionBoList;//红包分布

    private Integer MaxRedPacketIndex;//最大金额位置

    private Integer redPacketTotalNum; //红包总数


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public Integer getActivityIssue() {
        return activityIssue;
    }

    public void setActivityIssue(Integer activityIssue) {
        this.activityIssue = activityIssue;
    }

    public Double getRedPacketDownRate() {
        return redPacketDownRate;
    }

    public void setRedPacketDownRate(Double redPacketDownRate) {
        this.redPacketDownRate = redPacketDownRate;
    }

    public Integer getRedPacketNum() {
        return redPacketNum;
    }

    public void setRedPacketNum(Integer redPacketNum) {
        this.redPacketNum = redPacketNum;
    }

    public Integer getPrizeRedPacketNum() {
        return prizeRedPacketNum;
    }

    public void setPrizeRedPacketNum(Integer prizeRedPacketNum) {
        this.prizeRedPacketNum = prizeRedPacketNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getAllowCustNum() {
        return allowCustNum;
    }

    public void setAllowCustNum(Integer allowCustNum) {
        this.allowCustNum = allowCustNum;
    }

    public Integer getAllowJoinNum() {
        return allowJoinNum;
    }

    public void setAllowJoinNum(Integer allowJoinNum) {
        this.allowJoinNum = allowJoinNum;
    }

    public Integer getCustAllowJoinNum() {
        return custAllowJoinNum;
    }

    public void setCustAllowJoinNum(Integer custAllowJoinNum) {
        this.custAllowJoinNum = custAllowJoinNum;
    }

    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Boolean getIsDelete() {
        return isDelete;
    }


    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }


    public Integer getCustAllowWinningNum() {
        return custAllowWinningNum;
    }


    public void setCustAllowWinningNum(Integer custAllowWinningNum) {
        this.custAllowWinningNum = custAllowWinningNum;
    }


    public Integer getTimeInterval() {
        return timeInterval;
    }


    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }


    public Double getProbability() {
        return probability;
    }


    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public List<RedPacketDistributionBo> getDistributionBoList() {
        return distributionBoList;
    }

    public void setDistributionBoList(List<RedPacketDistributionBo> distributionBoList) {
        this.distributionBoList = distributionBoList;
    }

    public Integer getMaxRedPacketIndex() {
        return MaxRedPacketIndex;
    }

    public void setMaxRedPacketIndex(Integer maxRedPacketIndex) {
        MaxRedPacketIndex = maxRedPacketIndex;
    }

    public Integer getRedPacketTotalNum() {
        return redPacketTotalNum;
    }

    public void setRedPacketTotalNum(Integer redPacketTotalNum) {
        this.redPacketTotalNum = redPacketTotalNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
