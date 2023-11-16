package com.eshore.common.enums;

import com.eshore.common.utils.StringUtils;
import lombok.Getter;

/**
 * @author jianlin.liu
 * @date 2022/7/21 11:16
 * @version 1.0
 */
@Getter
public enum UserType {

    ADMINISTRATOR("0","administrator","超级管理员"),
    COMMON("00", "common__key","系统用户"),
    OPERATOR("01", "operator__key","运维人员"),
    SAFER("02", "safer__key","安全员"),
    ;

    private final String code;
    private final String nameEn;
    private final String desc;

    UserType(String code,String nameEn, String desc) {
        this.code = code;
        this.nameEn = nameEn;
        this.desc = desc;
    }

    public static UserType findByCode(String code) {
        if(StringUtils.isEmpty(code)) return null;
        for (UserType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
