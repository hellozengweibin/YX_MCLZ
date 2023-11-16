package com.eshore.videoTransfer.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetRealPlayUrlResp extends VideoConvertBaseResp {
    private ResultDTO result;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        private PlayUrlDTO playUrl;

        @NoArgsConstructor
        @Data
        @ApiModel(value = "PlayUrlDTO")
        public static class PlayUrlDTO {
            @ApiModelProperty(value = "rtmp地址流")
            private String rtmpUrl;
            @ApiModelProperty(value = "hls地址流")
            private String hlsUrl;
            @ApiModelProperty(value = "flv地址流")
            private String flvUrl;
        }
    }
}
