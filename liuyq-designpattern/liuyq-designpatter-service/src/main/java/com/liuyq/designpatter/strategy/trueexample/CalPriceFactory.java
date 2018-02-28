package com.liuyq.designpatter.strategy.trueexample;

/**
 * Created by liuyq on 2017/12/11.
 */
//获取应该用什么策略来计算金额(静态工厂模式)
public class CalPriceFactory {

    private CalPriceFactory(){}

    public static CalPrice getCalPrice(Customer customer){
        if (customer.getTotalAmount() > 3000) {//3000则改为金牌会员计算方式
            return new GoldVip();
        }else if (customer.getTotalAmount() > 2000) {//类似
            return new SuperVip();
        }else if (customer.getTotalAmount() > 1000) {//类似
            return new Vip();
        }else {
            return new Common();
        }
    }
}
