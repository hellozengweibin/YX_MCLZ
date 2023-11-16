package com.eshore.videoTransfer.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TimeSpan
 * @Description
 * @Author jianlin.liu
 * @Date 2023/6/26
 * @Version 1.0
 **/
@Data
@ApiModel(value = "TimeSpan", description = "时间范围")
public class TimeSpan {
    /**
     * 开始时间（UTC时间）
     * 格式如yyyyMMddHHmmss，例如：20121207102035，长度限制20字节
     */
    @ApiModelProperty("开始时间（UTC时间） 格式如yyyyMMddHHmmss，例如：20121207102035，长度限制20字节")
    private String startTime;

    /**
     * 结束时间（UTC时间）
     * 格式如yyyyMMddHHmmss，例如：20121207102035，长度限制20字节
     */
    @ApiModelProperty("结束时间（UTC时间） 格式如yyyyMMddHHmmss，例如：20121207102035，长度限制20字节")
    private String endTime;

    public String getStartTime() {
        return startTime;
//        return DateUtils.toUTC(startTime);
    }

    public String getEndTime() {
        return endTime;
//        return DateUtils.toUTC(endTime);
    }
}
