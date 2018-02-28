package com.liuyq.designpatter.strategy.notifelse;

/**
 * Created by liuyq on 2017/12/11.
 *   就比如我们要做一个商店的收银系统，
 *   这个商店有普通顾客，会员，超级会员以及金牌会员的区别，针对各个顾客，有不同的打折方式，
 *   并且一个顾客每在商店消费1000就增加一个级别，那么我们就可以使用策略模式，因为策略模式描述的就是算法的不同，
 *   而且这个算法往往非常繁多，并且可能需要经常性的互相替换。
 */
//计算该付多少钱的接口
public interface CalPrice {

    Double calPrice(Double originalPrice);
}
