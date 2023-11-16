package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/13 10:03
 */
@Data
public class SearchFaceResponse {

    private String vision;

    private Integer num;

    @Alias("feature_list")
    private List<FaceFeature> featureList;

    private Integer type;

    @Data
    public static class FaceFeature {

        private String featureData;

        @Alias("create_time")
        private String createTime;

        @Alias("name_type")
        private String nameType;

        private String featureId;

    }

}
