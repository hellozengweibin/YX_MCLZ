package com.eshore.videoTransfer.req;

import lombok.Builder;
import lombok.Data;

/**
* deviceId	是	是	String	设备id。全局唯一，不超过36字节
 * rtspUrl	是	是	String	Rtsp地址（含密码）
 * index	是	是	Int	码流类型。1-第一码流（主码流）；2-第二码流（子码流）3-第三码流
*/
@Data
@Builder
public class BindDeviceReq {

    private String deviceId;
    private String rtspUrl;
    private Integer index;

}
