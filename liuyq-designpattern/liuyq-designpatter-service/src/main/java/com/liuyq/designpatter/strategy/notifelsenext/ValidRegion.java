package com.liuyq.designpatter.strategy.notifelsenext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyq on 2017/12/13.
 */
//嵌套注解
@Target(ElementType.ANNOTATION_TYPE) //表示只能给注解添加该注解
@Retention(RetentionPolicy.RUNTIME) //这个必须要将注解保留在运行时
public @interface ValidRegion {
    int max() default Integer.MAX_VALUE;
    int min() default Integer.MIN_VALUE;

    //既然可以任意组合，我们就需要给策略定义下顺序，就比如刚才说的2000那个例子，按先返后打折的顺序是800，反过来就是600了。
    //所以我们必须支持这一特性，默认0，为最优先
    int order() default 0;

}
