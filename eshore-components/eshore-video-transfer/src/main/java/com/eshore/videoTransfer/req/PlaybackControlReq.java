package com.eshore.videoTransfer.req;

import io.swagger.annotations.ApiModelProperty;

public class PlaybackControlReq {
    private String streamId;

    /**
     * 控制指令代码
     * 1：pause,
     * 2：resume,
     * 3：seek,
     * 4：scale
     */
    @ApiModelProperty("控制指令代码 1：pause, 2：resume,3：seek,4：scale")
    private Integer cmd;
    /**
     * cmd=3时有必填，相对url中起始时间的偏移值，单位：秒，3600为1小时
     */
    @ApiModelProperty("cmd=3时有必填，相对url中起始时间的偏移值，单位：秒，3600为1小时")
    private Integer seek;
    /**
     * cmd=4时有必填，播放倍速，大于0为快进，小于0为慢进
     */
    @ApiModelProperty("cmd=4时有必填，播放倍速，大于0为快进，小于0为慢进")
    private Integer scale;
}
