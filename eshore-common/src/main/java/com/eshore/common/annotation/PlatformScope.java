package com.eshore.common.annotation;

import com.eshore.common.enums.PlatformType;

import java.lang.annotation.*;

/**
 * 自定义注解----用于限制接口只能在指定的平台才能调用，否则不允许调用
 *
 * @author jianlin.liu
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PlatformScope {
    /**
     * 平台类型，支持多个
     */
    PlatformType[] types() default {};

    /**
     * 提示消息
     */
    String message() default "此接口在该平台不允许调用";
}
