package com.eshore.feign.gd_telecom_ai.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanglangxin
 * @since 2022/7/13 10:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchFaceRequest extends GrpRequest {

    /**
     * 人脸库
     */
    private String groupName;

    /**
     * 人脸 Id, 特征 Id
     */
    private String featureId;

    /**
     * 1:黑名单-2:白名单
     */
    private Integer nameType;

    /**
     * 最大查询数量
     */
    private Integer limit;

    /**
     * 偏移量
     */
    private Integer offset;
}
