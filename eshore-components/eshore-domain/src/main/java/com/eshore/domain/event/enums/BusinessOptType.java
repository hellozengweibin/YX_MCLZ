package com.eshore.domain.event.enums;

import lombok.Getter;

@Getter
public enum BusinessOptType {

    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    ;

    private int code;

    private String desc;


    BusinessOptType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public BusinessOptType findByCode(Integer code) {
        if (code == null) return null;
        for (BusinessOptType value : values()) {
            if (value.code == code.intValue()) {
                return value;
            }
        }
        return null;
    }

}
