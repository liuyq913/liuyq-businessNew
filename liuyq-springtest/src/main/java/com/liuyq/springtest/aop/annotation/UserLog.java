package com.liuyq.springtest.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by liuyq on 2019/4/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited //允许子类继承父类的注解。
public @interface UserLog {
    String description() default "";
}
