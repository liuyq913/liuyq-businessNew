package com.liuyq.designpatter.strategy.notifelsenext;

/**
 * Created by liuyq on 2017/12/11.
 * 就是说我们同一时间只能采用一种策略，假设我们商店现在有这么一个需求，
 * 假设到端午节了，我们商店要采取满1000返200，满2000返400的方式，并且原有的打折还要继续，
 * 这就相当于将返现金的活动与打折重叠计算了。

 比如我是个金牌会员，假设我买了2000的东西，那么计算方式应该是先减去400为1600，
 再打五折，为800。最后这个会员只需要付800

 现在我们的需求变了，即我们任意的策略都可以随意组合，并且我们要求工厂帮我们自动判断，并将策略叠加返回给我们
 */
//计算该付多少钱的接口
public interface CalPrice {

    Double calPrice(Double originalPrice);
}
