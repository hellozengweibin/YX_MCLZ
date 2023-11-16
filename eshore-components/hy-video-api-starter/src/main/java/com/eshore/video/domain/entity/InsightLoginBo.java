package com.eshore.video.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 慧眼登录表(视频流服务中实体) ymh 2023-03-24
 */
@Data
@ApiModel("慧眼登录表")
public class InsightLoginBo {

    @ApiModelProperty("主键")
    private Integer id;

    /**
    * 用户名
    */
    @ApiModelProperty("用户名")
    private String userName;

    /**
    * 用户密码
    */
    @ApiModelProperty("用户密码")
    private String userSecret;

    /**
    * 区域编码
    */
    @ApiModelProperty("区域编码")
    private String areaCode;

    /**
    * 登录地址
    */
    @ApiModelProperty("登录地址")
    private String loginUrl;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

}

