package com.eshore.videoTransfer.req;

import lombok.Builder;
import lombok.Data;

/**
* 设备id。全局唯一，不超过36字节
*/
@Data
@Builder
public class UnbindDeviceReq {
    private String deviceId;
}
