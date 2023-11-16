package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 17:58
 */
@Data
public class SaveFaceResponse {

    /**
     * 失败列表 featureId
     */
    @Alias("fail_list")
    private List<String> failList;

    /**
     * 成功列表 featureId
     */
    @Alias("success_list")
    private List<String> successList;

}
