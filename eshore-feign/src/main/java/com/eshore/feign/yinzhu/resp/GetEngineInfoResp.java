package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class GetEngineInfoResp extends BaseResp{
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private List<TTSEngineInfoDTO> tTSEngineInfo;

        @NoArgsConstructor
        @Data
        public static class TTSEngineInfoDTO {
            private Integer engineIndex;
            private String engineName;
        }
    }
}
