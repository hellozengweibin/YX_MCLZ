package com.eshore.video.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shanglangxin
 * @since 2023/1/17 10:21
 */
@Data
public class GetPlaybackUrlDto {
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "视频存储 云端和前端存储，前端获取时间轴的时候能得知存储类型")
    private Integer storageType;
}
