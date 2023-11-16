package com.eshore.videoTransfer.req;

import io.swagger.annotations.ApiModelProperty;

public class NetworkParam {
    /**
     * 网络类型：
     * 1：私网
     * 2：公网
     * 该值只有domainType为1才生效
     */
    @ApiModelProperty("网络类型： 1：私网 ;2：公网 该值只有domainType为1才生效")
    private Integer netType;

    /**
     * 域类型（默认为1）
     * 1：无域名(ip+port)
     * 2：域名
     */
    @ApiModelProperty(" 域类型（默认为1） 1：无域名(ip+port) ;2：域名")
    private Integer domainType=1;



    @ApiModelProperty(" http类型（默认为1）： 1: http: 2: https")
    private Integer httpType=1;
}
