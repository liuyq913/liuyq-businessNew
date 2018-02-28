package com.liuyq.designpatter.strategy.notifelsenext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyq on 2017/12/13.
 */
//总额有效区间注解，可以给策略添加有效的区间的设置
@Target(ElementType.TYPE) //表示给类添加注解
@Retention(RetentionPolicy.RUNTIME) //这个必须要将注解保留在运行时
public @interface TotalValidRegion {
    //引用有效区间注解
    ValidRegion value() default @ValidRegion;
}
