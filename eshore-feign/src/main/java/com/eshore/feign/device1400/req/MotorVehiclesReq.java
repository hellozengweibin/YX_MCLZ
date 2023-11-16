package com.eshore.feign.device1400.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author:walker
 * time: 2022/7/20 0020 10:55
 * description:  激动车辆回调参数
 */


@NoArgsConstructor
@Data
public class MotorVehiclesReq {

    @JsonProperty(value = "MotorVehicleListObject")
    private MotorVehicleListObject MotorVehicleListObject;

    @NoArgsConstructor
    @Data
    public static class MotorVehicleListObject {
        @JsonProperty(value = "MotorVehicleObject")
        private List<MotorVehicleListObject.MotorVehicleObject> MotorVehicleObject;

        @NoArgsConstructor
        @Data
        public static class MotorVehicleObject {
            @JsonProperty(value = "MotorVehicleID")
            private String MotorVehicleID;
            @JsonProperty(value = "InfoKind")
            private Integer InfoKind;
            @JsonProperty(value = "SourceID")
            private String SourceID;
            @JsonProperty(value = "DeviceID")
            private String DeviceID;
            @JsonProperty(value = "StorageUrl1")
            private String StorageUrl1;
            @JsonProperty(value = "LeftTopX")
            private Integer LeftTopX;
            @JsonProperty(value = "LeftTopY")
            private Integer LeftTopY;
            @JsonProperty(value = "RightBtmX")
            private Integer RightBtmX;
            @JsonProperty(value = "RightBtmY")
            private Integer RightBtmY;
            @JsonProperty(value = "MarkTime")
            private String MarkTime;
            @JsonProperty(value = "AppearTime")
            private String AppearTime;
            @JsonProperty(value = "LaneNo")
            private Integer LaneNo;
            @JsonProperty(value = "HasPlate")
            private String HasPlate;
            @JsonProperty(value = "PlateClass")
            private String PlateClass;
            @JsonProperty(value = "PlateColor")
            private String PlateColor;
            @JsonProperty(value = "PlateNo")
            private String PlateNo;
            @JsonProperty(value = "VehicleColor")
            private String VehicleColor;
            @JsonProperty(value = "PassTime")
            private String PassTime;
            @JsonProperty(value = "SubImageList")
            private MotorVehicleObject.SubImageList SubImageList;

            @NoArgsConstructor
            @Data
            public static class SubImageList {
                @JsonProperty(value = "SubImageInfoObject")
                private List<SubImageList.SubImageInfoObject> SubImageInfoObject;

                @NoArgsConstructor
                @Data
                public static class SubImageInfoObject {
                    @JsonProperty(value = "ImageID")
                    private String ImageID;
                    @JsonProperty(value = "EventSort")
                    private Integer EventSort;
                    @JsonProperty(value = "DeviceID")
                    private String DeviceID;
                    @JsonProperty(value = "StoragePath")
                    private String StoragePath;
                    @JsonProperty(value = "Type")
                    private String Type;
                    @JsonProperty(value = "FileFormat")
                    private String FileFormat;
                    @JsonProperty(value = "ShotTime")
                    private String ShotTime;
                    @JsonProperty(value = "Width")
                    private Integer Width;
                    @JsonProperty(value = "Height")
                    private Integer Height;
                    @JsonProperty(value = "FileSize")
                    private Integer FileSize;
                    @JsonProperty(value = "Data")
                    private String Data;
                }
            }
        }
    }
}
