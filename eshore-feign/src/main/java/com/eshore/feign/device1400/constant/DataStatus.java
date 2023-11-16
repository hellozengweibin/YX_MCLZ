package com.eshore.feign.device1400.constant;

public enum DataStatus {

    VAILD(1, "有效"),
    INVAILD(0, "无效");

    private Integer code;
    private String desc;

    DataStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return this.code;
    }
}
