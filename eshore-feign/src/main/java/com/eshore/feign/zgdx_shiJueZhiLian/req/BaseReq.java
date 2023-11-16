package com.eshore.feign.zgdx_shiJueZhiLian.req;

import lombok.Data;

@Data
public class BaseReq {
    private String sign;
    private String appkey;
    private Object parmdata;
}
