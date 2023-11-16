package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * @author shanglangxin
 * @since 2022/7/13 10:21
 */
@Data
public class SaveFaceLibResponse {

    @Alias("group_name")
    private String groupName;

}
