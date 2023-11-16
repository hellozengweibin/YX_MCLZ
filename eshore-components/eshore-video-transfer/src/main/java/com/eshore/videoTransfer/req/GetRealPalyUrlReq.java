package com.eshore.videoTransfer.req;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* deviceId	是	是	String	设备id。全局唯一
 * index	是	是	Int	码流类型。1-第一码流（主码流）；2-第二码流（子码流）3-第三码流
*/

@NoArgsConstructor
@Data
@Builder
public class GetRealPalyUrlReq {
    private String deviceId;
    private Integer index;

    public GetRealPalyUrlReq(String deviceId, Integer index) {
        this.deviceId = deviceId;
        this.index = index;
    }
}
