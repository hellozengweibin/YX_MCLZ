package com.eshore.video.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author cjy
 * @Date 2022/7/14 9:07
 * @Version 1.0
 */
@Data
@ApiModel("视频回放传参")
public class SearchVideoDto {
    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    private Long id;

    /**
     * 设备id
     */
    @ApiModelProperty(name = "设备国标编码", example = "44195153811327126025")
    private String deviceId;

    /**
     * 告警的创建时间2022-08-12 17:22:50
     */
    @ApiModelProperty(name = "查询回放时间点", example = "2022-08-12 17:22:50")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alarmTime;

    @ApiModelProperty(name = "查询的开始时间", example = "2022-08-12 17:22:50", notes = "录像下载只能获取当天的录像数据")
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(name = "查询的结束时间", example = "2022-08-12 17:23:50")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String start;

    private String end;

}
