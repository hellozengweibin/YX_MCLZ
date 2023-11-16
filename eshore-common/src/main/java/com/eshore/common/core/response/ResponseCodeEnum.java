package com.eshore.common.core.response;

import com.eshore.common.constant.HttpStatus;

/**
 * @author jianlin.liu
 * @since 2022/07/01
 * 响应码枚举
 */
public enum ResponseCodeEnum {
    /**
     * 成功
     */
    SUCCESS(HttpStatus.SUCCESS),
    /**
     * 失败
     */
    FAIL(HttpStatus.ERROR),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED);
    private final int code;

    ResponseCodeEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
