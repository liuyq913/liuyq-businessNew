package com.liuyq.redpacket.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 10731 on 2017/6/28.
 */
public class RedPacketPojo implements Serializable{

    private static final long serialVersionUID = 5016714339005638422L;

    private String uuid;

    private BigDecimal packetPrice;

    private Integer prizeId;

    public RedPacketPojo(String uuid, BigDecimal packetPrice) {
        this.uuid = uuid;
        this.packetPrice = packetPrice;
    }

    public RedPacketPojo(String uuid, Integer prizeId) {
        this.uuid = uuid;
        this.prizeId = prizeId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getPacketPrice() {
        return packetPrice;
    }

    public void setPacketPrice(BigDecimal packetPrice) {
        this.packetPrice = packetPrice;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedPacketPojo that = (RedPacketPojo) o;

        return uuid != null ? !uuid.equals(that.uuid) : that.uuid != null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        return result;
    }
}
