package com.eshore.videoTransfer.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BindDeviceListResp extends VideoConvertBaseResp {
    private List<BindListDTO> bindList;

    @NoArgsConstructor
    @Data
    public static class BindListDTO {
        private String deviceId;
        private Integer online;
        private List<StreamListDTO> streamList;

        @NoArgsConstructor
        @Data
        public static class StreamListDTO {
            private String rtspUrl;
            private Integer index;
        }
    }
}
