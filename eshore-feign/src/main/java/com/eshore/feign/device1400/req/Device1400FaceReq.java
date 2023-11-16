package com.eshore.feign.device1400.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


/**
 * 由于@RequestBody无法转换首字母为大写的参数，所以需要加上@JsonProperty
 */
@Data
public class Device1400FaceReq {
    @JsonProperty(value = "FaceListObject")
    private FaceListObjectDTO faceListObject;

    @Data
    public static class FaceListObjectDTO {
        @JsonProperty(value = "FaceObject")
        private List<FaceObjectDTO> faceObject;

        @Data
        public static class FaceObjectDTO {
            @JsonProperty(value = "FaceID")
            private String faceID;
            @JsonProperty(value = "InfoKind")
            private Integer infoKind;
            @JsonProperty(value = "SourceID")
            private String sourceID;
            @JsonProperty(value = "DeviceID")
            private String deviceID;
            @JsonProperty(value = "LeftTopX")
            private Integer leftTopX;
            @JsonProperty(value = "LeftTopY")
            private Integer leftTopY;
            @JsonProperty(value = "RightBtmX")
            private Integer rightBtmX;
            @JsonProperty(value = "RightBtmY")
            private Integer rightBtmY;
            @JsonProperty(value = "FaceAppearTime")
            private String faceAppearTime;
            @JsonProperty(value = "FaceDisAppearTime")
            private String faceDisAppearTime;
            @JsonProperty(value = "LocationMarkTime")
            private String locationMarkTime;
            @JsonProperty(value = "AssociatedPersonID")
            private String associatedPersonID;
            @JsonProperty(value = "SubImageList")
            private SubImageListDTO subImageList;


            @Data
            public static class SubImageListDTO {
                @JsonProperty(value = "SubImageInfoObject")
                private List<SubImageInfoObjectDTO> subImageInfoObject;

                @Data
                public static class SubImageInfoObjectDTO {
                    @JsonProperty(value = "ImageID")
                    private String imageID;
                    @JsonProperty(value = "EventSort")
                    private Integer eventSort;
                    @JsonProperty(value = "DeviceID")
                    private String deviceID;
                    @JsonProperty(value = "Type")
                    private String type;
                    @JsonProperty(value = "FileFormat")
                    private String fileFormat;
                    @JsonProperty(value = "ShotTime")
                    private String shotTime;
                    @JsonProperty(value = "Width")
                    private Integer width;
                    @JsonProperty(value = "Height")
                    private Integer height;
                    @JsonProperty(value = "Data")
                    private String data;
                }
            }
        }
    }
}
