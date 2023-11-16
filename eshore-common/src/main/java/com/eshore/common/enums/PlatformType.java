package com.eshore.common.enums;

import lombok.Getter;

/**
 * @EnumName PlatformType
 * @Description 平台类型枚举
 * @Author jianlin.liu
 * @Date 2022/9/15
 * @Version 1.0
 **/
@Getter
public enum PlatformType {

    TYPE_1(1, "management", "管理端", true),
    TYPE_2(2, "client", "客户端", true),
    APP(5, "APP", "移动端", false),
    ;

    private int code;
    private String nameEn;
    private String name;
    private boolean supportSwitch;

    PlatformType(int code, String nameEn, String desc, boolean supportSwitch) {
        this.code = code;
        this.nameEn = nameEn;
        this.name = desc;
        this.supportSwitch = supportSwitch;
    }

    public static PlatformType findByCode(int code) {
        for (PlatformType value : values()) {
            if (code == value.getCode()) {
                return value;
            }
        }
        return null;
    }

}
