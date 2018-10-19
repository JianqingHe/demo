package com.sp.support;

import java.lang.annotation.*;

/**
 * 自定义校验注解
 *
 * @author hejq
 * @date 2018-07-28 11:43
 */
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    int min() default 1;

    int max() default 10;

    boolean isNotNull() default true;

}
