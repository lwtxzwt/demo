package com.lwtxzwt.demo.annotation;

import java.lang.annotation.*;

/**
 * 流量控制注解
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    /**
     * 默认最大值100
     */
    int max() default 100;

    /**
     * 标志key
     */
    String key();

}
