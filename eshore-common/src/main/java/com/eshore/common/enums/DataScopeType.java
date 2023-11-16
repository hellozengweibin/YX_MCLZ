package com.eshore.common.enums;

import com.eshore.common.utils.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @EnumName DataScopeType
 * @Description 数据权限类型
 * @Author jianlin.liu
 * @Date 2023/6/30
 * @Version 1.0
 **/
@Slf4j
@Getter
public enum DataScopeType {
    DEPT(1, "dept", "dept_id"),
    PLATFORM(2, "platform", "platform_type"),
    CREATER(3, "creater", "create_by"),
    ;

    private int code;

    private String name;

    private String columnName;

    DataScopeType(int code, String name, String columnName) {
        this.code = code;
        this.name = name;
        this.columnName = columnName;
    }

    public static DataScopeType findByCode(int code) {
        for (DataScopeType value : values()) {
            if (code == value.code) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取权限过滤器实例
     * @param
     * @return
     */
    public Object getDataScopeFilterInstance() {
        if (StringUtils.isEmpty(name)) return null;
        // 约定 DataScopeFilter所在的包名为com.eshore.framework.datascope
        String dataScopeFilterPackage = "com.eshore.framework.datascope.";
        String prefix = name.substring(0, 1).toUpperCase() + name.substring(1);
        String filterName = prefix + "DataScopeFilter";
        String className = dataScopeFilterPackage + filterName;
        try {
            Class<?> clzz = Class.forName(className);
            Object instance = clzz.newInstance();
            return instance;
        } catch (Exception e) {
            log.error("[getDataScopeFilterInstance]>>>>>>>>>>>>get filter[{}] instance error:{}", className, e);
        }
        return null;
    }
}
