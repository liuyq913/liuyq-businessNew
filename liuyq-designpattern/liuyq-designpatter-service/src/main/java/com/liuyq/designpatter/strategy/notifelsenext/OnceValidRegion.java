package com.liuyq.designpatter.strategy.notifelsenext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyq on 2017/12/13.
 */
//针对单次消费的有效区间注解，可以给策略添加有效区间的设置
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnceValidRegion {
    //引用有效区间注解
    ValidRegion value() default @ValidRegion;
}
