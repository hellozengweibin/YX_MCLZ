package com.eshore.feign.gd_telecom_ai.request;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 17:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaveFaceRequest extends GrpRequest {

    /**
     * 人脸库名称
     */
    @Alias("group_name")
    private String groupName;

    /**
     * 上传类型: 1:FACE-2:BODY
     */
    private Integer type;

    /**
     * 算法版本，默认 2
     */
    private String version;

    /**
     * 上传特征值
     */
    @Alias("feature_list")
    private List<FaceFeature> featureList;

    @Data
    public static class FaceFeature {

        /**
         * 上传 Id, 自定义
         */
        private String featureId;

        /**
         * 特征值
         */
        private String featureData;

        /**
         * 1:黑名单-2:白名单
         */
        @Alias("name_type")
        private Integer nameType;
    }
}
