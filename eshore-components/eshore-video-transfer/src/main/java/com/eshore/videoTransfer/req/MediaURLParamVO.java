package com.eshore.videoTransfer.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName VideoPlayParamVO
 * @Description
 * @Author jianlin.liu
 * @Date 2023/6/26
 * @Version 1.0
 **/
@Data
public class MediaURLParamVO {

    /**
     * 目前仅支持业务类型：
     * 1：实时浏览
     * 3：本地录像下载
     * 4：平台录像回放
     * 5：语音对讲
     * 6：语音广播
     * 8：前端录像回放
     */
    @ApiModelProperty("目前仅支持业务类型： 1：实时浏览 3：本地录像下载 4：平台录像回放 5：语音对讲 6：语音广播 8：前端录像回放")
    @NotNull(message = "业务类型不能为空")
    @Min(value = 1, message = "业务类型参数最小值为1")
    @Max(value = 8, message = "业务类型参数最大值为8")
    private Integer serviceType = 1;

    /**
     * 码流类型（默认为1）：
     * 1：主码流
     * 2：辅码流1
     * 3：辅码流2
     */
    @ApiModelProperty("码流类型（默认为1）： 1：主码流 2：辅码流1 3：辅码流2")
    @Min(value = 1, message = "streamType最小值为1")
    @Max(value = 3, message = "streamType最大值为3")
    private Integer streamType = 1;

    /**
     * 录像的开始和结束时间，如果是平台或前端录像回放、下载需要时必填
     * 说明： 一般通过获取录像列表查到，和返回的录像时间段统一。
     */
    @ApiModelProperty("录像的开始和结束时间，如果是平台或前端录像回放、下载需要时必填 说明： 一般通过获取录像列表查到，和返回的录像时间段统一。")
    private TimeSpan timeSpan;

    /**
     * （映射环境时必填）
     * 客户端类型（可填参数）：
     * 0：IVS CU或eSDK
     * 1：标准RTSP客户端
     * 5：RTSP短URL客户端
     * 说明：clientType为5时返回短URL;针对外域设备或者海康解码器上墙场景，需传入该
     * 值
     */
    @ApiModelProperty("客户端类型（非可填参数，默认1）： 0：IVS CU或eSDK 1：标准RTSP客户端 5：RTSP短URL客户端 说明：clientType为5时返回短URL;针对外域设备或者海康解码器上墙场景，需传入该 值")
    private Integer clientType = 1;

    /**
     * 录像文件名称，可填参数。前端录像回放及下载时必填。不超过64个字符。
     * 说明：一般通过查询录像获取到，和返回的录像文件名统一。
     */
    @ApiModelProperty("录像文件名称，可填参数。前端录像回放及下载时必填。不超过64个字符。 说明：一般通过查询录像获取到，和返回的录像文件名统一。")
    private String fileName;

    /**
     * 协议类型（默认为1）：
     * 1：UDP
     * 2：TCP
     * 说明：推荐使用TCP
     */
    @ApiModelProperty("码流类型（默认为1）： 1：UDP 2：TCP ")
    @Min(value = 1, message = "protocolType最小值为1")
    @Max(value = 2, message = "protocolType最大值为2")
    private Integer protocolType = 2;

    /**
     * 打包协议类型
     * 1：ES（默认）
     * 2：PS
     * 3：TS
     * 说明： PS用于国标摄像机；一般ES比较多，推荐选择ES。
     */
    private  Integer packProtocolType=1;

    private NetworkParam networkParam;

    @ApiModelProperty("rtsp播放地址 ")
    private String rtspURL;
}
