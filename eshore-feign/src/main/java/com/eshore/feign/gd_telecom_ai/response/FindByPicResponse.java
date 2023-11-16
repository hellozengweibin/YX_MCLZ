package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 15:42
 */
@Data
public class FindByPicResponse {

    private Integer num;

    @Alias("feature_list")
    private List<FaceFeature> featureList;

    @Data
    public static class FaceFeature {

        private Double score;

        @Alias("name_type")
        private Integer nameType;

        private String featureType;

        private String featureId;

    }
}
