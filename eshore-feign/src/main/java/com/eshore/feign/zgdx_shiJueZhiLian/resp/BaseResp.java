package com.eshore.feign.zgdx_shiJueZhiLian.resp;

import lombok.Data;

@Data
public class BaseResp<T> {
    private String code;
    private String msg;
    private T result;
}
