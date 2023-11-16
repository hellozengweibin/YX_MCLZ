package com.eshore.feign.gd_telecom_ai.response;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 17:09
 */
@Data
public class AnalyseImagesForFaceResponse {

    @Alias("data")
    private List<AnalyseImage> imageList;

    private Integer num;

    @Data
    public static class Angle {
        private Double x;

        private Double y;

        private Double z;
    }

    @Data
    public static class Attributes {
        private Double confidence;

        private String name;

        private String value;
    }

    @Data
    public static class AnalyseImage {
        private String featureData;

        private Double score;

        private List<Integer> bbox;

        private String featureType;

        private Angle angle;

        private List<Attributes> attributes;

        private String version;
    }

}
