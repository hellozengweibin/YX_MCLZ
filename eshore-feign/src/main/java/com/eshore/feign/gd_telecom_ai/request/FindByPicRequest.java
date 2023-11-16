package com.eshore.feign.gd_telecom_ai.request;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanglangxin
 * @since 2022/7/11 12:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FindByPicRequest extends GrpRequest {

    /**
     * 人脸库名称
     */
    @Alias("group_name")
    private String groupName;

    /**
     * 特征值
     */
    private String featureData;

    /**
     * 查询数量
     */
    private Integer limit;

    /**
     * 相似度, 百分比
     */
    private String threshold;

}
