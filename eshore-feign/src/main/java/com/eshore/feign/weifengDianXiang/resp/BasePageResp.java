package com.eshore.feign.weifengDianXiang.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class BasePageResp {
    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO<T> {
        private Integer lineSize;
        private Integer totalRecord;
        private Integer currentPage;
        private Integer startIndex;
        private Integer pageCount;
        private List<T> list;
    }
}
