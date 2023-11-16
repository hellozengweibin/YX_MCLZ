package com.eshore.feign.device1400.constant;

public enum PushPhotoType {
    FACE_PIC("FACE_PIC", "人脸"),
    MOTOR_PIC("MOTOR_PIC", "车辆图");

    private String code;
    private String desc;

    PushPhotoType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }
}
