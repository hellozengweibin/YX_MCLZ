package com.eshore.feign.gd_telecom_ai.request;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 19:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteFaceRequest extends GrpRequest {

    @Alias("feature_list")
    private List<String> featureList;

    @Alias("group_name")
    private String groupName;

}
