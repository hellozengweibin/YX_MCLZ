package com.eshore.feign.gd_telecom_ai.request;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanglangxin
 * @since 2022/7/13 10:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaveFaceLibRequest extends GrpRequest {

    /**
     * 库名
     */
    @Alias("group_name")
    private String groupName;

    /**
     * 服务模型版本， 默认使用：100
     */
    private String version;

    /**
     * 库描述
     */
    private String message;

    /**
     * 库类型: 1: FACE 人脸-2: BODY 人体
     */
    private Integer type;
}
