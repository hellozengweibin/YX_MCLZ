package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 19:45
 */
@Data
public class DeleteFaceResponse {

    @Alias("success_list")
    private List<String> successList;

    @Alias("fail_list")
    private List<String> failList;
}
