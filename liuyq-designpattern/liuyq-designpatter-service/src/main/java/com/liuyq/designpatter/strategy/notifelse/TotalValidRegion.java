package com.liuyq.designpatter.strategy.notifelse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyq on 2017/12/11.
 *
 * 这个注解是用来给策略添加的，当中可以设置它的上下限
 * */

//这是有效区间注解，可以给策略添加有效区间的设置
@Target(ElementType.TYPE) //表示只能给类添加该注解
@Retention(RetentionPolicy.RUNTIME) //这个必须要将注解保留在运行时
public @interface TotalValidRegion {
    int max() default Integer.MAX_VALUE;
    int min() default Integer.MIN_VALUE;
}
