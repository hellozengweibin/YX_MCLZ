package com.eshore.video.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author cjy
 * @Date 2022/12/26 16:29
 * @Version 1.0
 */
@Data
public class PTZHandlerDto {

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "控制指令代码")
    private String cmd;

    @ApiModelProperty(value = "自定义步进速度")
    private String speed;
    @ApiModelProperty(value = "自定义最小步进速度")
    private String minSpeed;
    @ApiModelProperty(value = "自定义最大步进速度")
    private String maxSpeed;

    @ApiModelProperty(value = "图像预置点ID")
    private String presetId;
    @ApiModelProperty(value = "图像巡航保留字段")
    private String presetTourId;
    @ApiModelProperty(value = "图像巡航保留字段")
    private String stayTime;

}
