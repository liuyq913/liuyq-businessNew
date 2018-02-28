package com.liuyq.designpatter.strategy.trueexample;

/**
 * Created by liuyq on 2017/12/11.
 */
public class Common implements CalPrice{
    public Double calPrice(Double originalPrice) {
        return originalPrice;
    }
}

class Vip implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice * 0.8;
    }

}
class SuperVip implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice * 0.7;
    }

}
class GoldVip implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice * 0.5;
    }

}