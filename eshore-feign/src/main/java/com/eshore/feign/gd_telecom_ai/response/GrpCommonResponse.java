package com.eshore.feign.gd_telecom_ai.response;

import lombok.Data;

/**
 * @author shanglangxin
 * @since 2022/7/12 11:23
 */
@Data
public class GrpCommonResponse {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态描述
     */
    private String message;

    /**
     * 请求UUID
     */
    private String appRequestId;

    /**
     * 以平台为准的交易发生时间，可以用于对账归属于哪一天
     */
    private String transTime;

    /**
     * 返回结果
     */
    private String result;

    public boolean isSuccess() {
        return 0 == code;
    }

}
