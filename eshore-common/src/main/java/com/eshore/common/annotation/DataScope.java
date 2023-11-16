package com.eshore.common.annotation;

import com.eshore.common.enums.DataScopeType;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 *
 * @author eshore
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataScopes.class)
public @interface DataScope {
    /**
     * 部门表的别名
     */
    public String deptAlias() default "";

    /**
     * 用户表的别名
     */
    public String userAlias() default "";

    /**
     * 平台类型别名
     */
    String platformAlias() default "";

    /**
     * 表别名
     * @return
     */
    String tableAlias() default "";

    /**
     * 数据权限类型
     */
    DataScopeType type() default DataScopeType.DEPT;

    /**
     * 是否使用find_in_set过滤
     * @return
     */
    boolean findInSet() default false;

    /**
     * 水域表别名
     */
    String waterAreaAlias() default "";

    String column() default "";

    /**
     * 是否跳过admin用户
     * @return
     */
    boolean skipAdmin() default true;
}
