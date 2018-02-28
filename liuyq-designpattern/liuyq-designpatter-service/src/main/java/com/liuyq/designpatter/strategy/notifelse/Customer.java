package com.liuyq.designpatter.strategy.notifelse;


/**
 * Created by liuyq on 2017/12/11.
 *
 * 客户类
 */
public class Customer {
    private Double totalAmount = 0D;//总共消费的金额

    private Double amount = 0D;//单次消费金额

    private CalPrice calPrice = new Common();//价格计算策略(默认原价)

    //购买商品
    public void buy(Double amount){
        this.amount = amount;
        totalAmount += amount;//总金额增加
        calPrice = CalPriceFactory.getInstance().createCalPrice(this);
    }

    //客户应该付的钱
    public Double calLastAmount(){
        return calPrice.calPrice(amount);
    }
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CalPrice getCalPrice() {
        return calPrice;
    }

    public void setCalPrice(CalPrice calPrice) {
        this.calPrice = calPrice;
    }
}
