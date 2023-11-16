package com.eshore.videoTransfer.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetPlayUrlResp extends BaseResp implements Serializable {

    @JsonProperty("data")
    private DataDTO data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class DataDTO implements Serializable {
        @JsonProperty("streamId")
        private String streamId;
        @JsonProperty("httpFlv")
        private String httpFlv;

        @JsonProperty("rtsp")
        private String rtsp;
        @JsonProperty("rtmp")
        private String rtmp;
        @JsonProperty("hls")
        private String hls;

    }
}
