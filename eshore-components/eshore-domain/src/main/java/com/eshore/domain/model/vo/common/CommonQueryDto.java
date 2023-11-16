package com.eshore.domain.model.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName CommonQueryDto
 * @Description 首页通用查询条件
 * @Author jianlin.liu
 * @Date 2023/1/9
 * @Version 1.0
 **/
@Data
public class CommonQueryDto {

    @ApiModelProperty(value = "层级id")
    private Long deptId;

    @ApiModelProperty(value = "算法类型id，多个使用逗号分隔")
    private String algorithmType;

    @ApiModelProperty(value = "开始时间，格式：yyyy-MM-dd HH:mm:ss 或者 yyyyMMddHHmmss")
    private String startTime;

    @ApiModelProperty(value = "结束时间，格式：yyyy-MM-dd HH:mm:ss 或者 yyyyMMddHHmmss")
    private String endTime;

    @ApiModelProperty(value = "时间间隔")
    private int timeRange;

    @ApiModelProperty(value = "localDate格式的开始结束时间")
    private LocalDateTime ld_startTime;
    private LocalDateTime ld_endTime;

    private List<Long> algorithmIds;


}
