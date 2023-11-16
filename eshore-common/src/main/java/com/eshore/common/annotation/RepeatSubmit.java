package com.eshore.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解防止表单重复提交
 *
 * @author eshore
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交，默认一分钟
     */
    int interval() default 60000;

    /**
     * 提示消息
     */
    String message() default "不允许重复提交，请稍候再试";

    /**
     * true 忽略重复请求
     *
     * @return
     */
    boolean enable() default true;
}
