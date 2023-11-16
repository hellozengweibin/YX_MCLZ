package com.eshore.feign.gd_telecom_ai.request;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanglangxin
 * @since 2022/7/13 10:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteFaceLibRequest extends GrpRequest {

    @Alias("group_name")
    private String groupName;
}
