package com.eshore.domain.annotation;

import lombok.Getter;

import java.lang.annotation.*;

/**
 * @AnnotationName FieldDesensitize
 * @Description 字段脱敏注解
 * @Author jianlin.liu
 * @Date 2023/7/26
 * @Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface FieldDesensitize {

    /**
     * 脱敏执行策略
     * @return
     */
    Strategy strategy() default Strategy.ID_CARD;



    @Getter
    public enum Strategy {
        /**
         * 身份证号
         */
        ID_CARD(1),
        /**
         * 手机号
         */
        PHONE_NUMBER(2),

        /**
         * 真实姓名
         */
        REAL_NAME(3),
        ;

        private final int value;

        Strategy(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
