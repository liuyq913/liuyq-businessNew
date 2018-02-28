package com.liuyq.designpatter.strategy.notifelsenext;


/**
 * Created by liuyq on 2017/12/11.
 */
@TotalValidRegion(@ValidRegion(max=1000,order=99))
public class Common implements CalPrice {
    public Double calPrice(Double originalPrice) {
        return originalPrice;
    }
}

@TotalValidRegion(@ValidRegion(min=1000,max=2000,order=99))
class Vip implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice * 0.8;
    }

}

@TotalValidRegion(@ValidRegion(min=2000,max=3000,order=99))
class SuperVip implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice * 0.7;
    }

}
@TotalValidRegion(@ValidRegion(min=3000,order=99))
class GoldVip implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice * 0.5;
    }

}


//即满1000返200和满2000返400，并且优先级高于打折，也就是说会先计算现金返回，再打折
@OnceValidRegion(@ValidRegion(min=1000,max=2000,order=40))
class OneTDTwoH implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice - 200;
    }

}
@OnceValidRegion(@ValidRegion(min=2000,order=40))
class TwotDFourH implements CalPrice{

    public Double calPrice(Double originalPrice) {
        return originalPrice - 400;
    }
}