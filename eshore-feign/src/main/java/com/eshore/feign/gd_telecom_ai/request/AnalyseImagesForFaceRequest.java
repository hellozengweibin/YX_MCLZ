package com.eshore.feign.gd_telecom_ai.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanglangxin
 * @since 2022/7/12 17:09
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AnalyseImagesForFaceRequest extends GrpRequest {

    /**
     * base64 编码 小于 2M
     */
    private String image;

    /**
     * 目前支持: FACE, BODY 人脸使用: FACE
     */
    private String type;

    /**
     * 算法版本, 默认 2
     */
    private String version;

}
