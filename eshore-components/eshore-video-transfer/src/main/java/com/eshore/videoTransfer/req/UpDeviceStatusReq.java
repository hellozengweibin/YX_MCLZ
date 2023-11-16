package com.eshore.videoTransfer.req;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
* deviceId	是	是	String	设备id。全局唯一
 * online	是	是	Int	在线状态。1在线；0离线
*/
@NoArgsConstructor
@Data
public class UpDeviceStatusReq {
    private String deviceId;
    private Integer online;
}
