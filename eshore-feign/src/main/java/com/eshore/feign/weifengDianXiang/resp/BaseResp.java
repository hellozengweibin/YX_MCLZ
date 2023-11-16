package com.eshore.feign.weifengDianXiang.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseResp {
    private Integer code;
    private String message;
}
