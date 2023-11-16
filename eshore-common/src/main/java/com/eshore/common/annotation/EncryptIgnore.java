package com.eshore.common.annotation;

import java.lang.annotation.*;

/**
 * @AnnotationName EncryptIgnore
 * @Description controller方法中如有此注解，表示返回不加密
 * @Author jianlin.liu
 * @Date 2022/9/13
 * @Version 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptIgnore {


}
