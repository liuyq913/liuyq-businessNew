package com.liuyq.redpacket.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gantianming on 2017/6/28.
 */
public class RedPacketDistributionBo implements Serializable{
    private static final long serialVersionUID = 185400261145189335L;

    private Integer ID;

    private Integer issueNum;

    private BigDecimal redPacketPrice;

    private Integer redPacketNum;

    private Boolean isDelete;

    private Date createTime;

    private Integer activityID;

    private Integer redPacketTotalNum;

    private Integer maxRedPacketLeftRange;

    private Integer maxRedPacketRightRange;

    private Integer prizeId;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(Integer issueNum) {
        this.issueNum = issueNum;
    }

    public BigDecimal getRedPacketPrice() {
        return redPacketPrice;
    }

    public void setRedPacketPrice(BigDecimal redPacketPrice) {
        this.redPacketPrice = redPacketPrice;
    }

    public Integer getRedPacketNum() {
        return redPacketNum;
    }

    public void setRedPacketNum(Integer redPacketNum) {
        this.redPacketNum = redPacketNum;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public Integer getRedPacketTotalNum() {
        return redPacketTotalNum;
    }

    public void setRedPacketTotalNum(Integer redPacketTotalNum) {
        this.redPacketTotalNum = redPacketTotalNum;
    }

    public Integer getMaxRedPacketLeftRange() {
        return maxRedPacketLeftRange;
    }

    public void setMaxRedPacketLeftRange(Integer maxRedPacketLeftRange) {
        this.maxRedPacketLeftRange = maxRedPacketLeftRange;
    }

    public Integer getMaxRedPacketRightRange() {
        return maxRedPacketRightRange;
    }

    public void setMaxRedPacketRightRange(Integer maxRedPacketRightRange) {
        this.maxRedPacketRightRange = maxRedPacketRightRange;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }
}
