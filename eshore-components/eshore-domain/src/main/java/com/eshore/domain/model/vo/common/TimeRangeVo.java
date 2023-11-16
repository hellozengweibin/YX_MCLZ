package com.eshore.domain.model.vo.common;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName TimeRangeVo
 * @Description 时间范围对象
 * @Author jianlin.liu
 * @Date 2023/7/12
 * @Version 1.0
 **/
@ApiModel(description = "时间范围对象")
@Data
public class TimeRangeVo {

    private String timeFormat = "%Y-%m-%d";

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    public TimeRangeVo(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeRangeVo(Date start, Date end) {
        if (start != null) {
            this.startTime = DateUtil.format(start, timeFormat);
        }
        if (end != null) {
            this.endTime = DateUtil.format(end, timeFormat);
        }
    }

    public TimeRangeVo(Date start, Date end, String timeFormat) {
        this.timeFormat = timeFormat;
        if (start != null) {
            this.startTime = DateUtil.format(start, timeFormat);
        }
        if (end != null) {
            this.endTime = DateUtil.format(end, timeFormat);
        }
    }
}
