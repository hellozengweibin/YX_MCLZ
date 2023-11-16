package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * @author shanglangxin
 * @since 2022/7/13 10:43
 */
@Data
public class SearchFaceLibResponse {

    @Alias("group_version")
    private String groupVision;

    @Alias("group_name")
    private String groupName;

    private Integer num;

    @Alias("groupType")
    private int groupType;

    private String massage;

    private Integer status;

}
