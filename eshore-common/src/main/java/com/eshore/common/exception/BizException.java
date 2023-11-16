package com.eshore.common.exception;

import lombok.Data;

/**
 * author:walker
 * time: 2022/6/15 0015 15:24
 * description:
 */
@Data
public class BizException extends RuntimeException {

    private String msg;

    public BizException(String msg) {
        this.msg = msg;
    }
}
