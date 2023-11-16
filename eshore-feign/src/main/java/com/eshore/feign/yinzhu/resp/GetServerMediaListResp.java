package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
* 示例参考下方
*/
@NoArgsConstructor
@Data
public class GetServerMediaListResp extends BaseResp{
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private MusicInfoDTO musicInfo;

        @NoArgsConstructor
        @Data
        public static class MusicInfoDTO {
            private List<DirArrayDTO> dirArray;
            private List<MusicArrayDTO> musicArray;

            @NoArgsConstructor
            @Data
            public static class DirArrayDTO {
                private Integer dirAudioTime;
                private Integer dirID;
                private String dirName;
            }

            @NoArgsConstructor
            @Data
            public static class MusicArrayDTO {
                private Integer audioId;
                private String audioName;
                private Integer dirID;
                private Integer duration;
            }
        }
    }


}
