package com.eshore.common.annotation;

import java.lang.annotation.*;

/**
 * @AnnotationName DataScopes
 * @Description 注解容器
 * @Author jianlin.liu
 * @Date 2023/7/7
 * @Version 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScopes {
    DataScope[] value();
}
