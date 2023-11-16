package com.eshore.feign.yinzhu.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 参数	必选	类型	说明
 * Tname : 分组名
 * call_code : 呼叫编码
 * terminals : 终端数组, 数组的元素为一个对象, 对象包含终端id
*/
@NoArgsConstructor
@Data
public class CreateGroupReq {
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "call_code")
    private Integer call_code;
    @JSONField(name = "terminals")
    private List<TerminalsDto> terminals;

    @NoArgsConstructor
    @Data
    public static class TerminalsDto{
        @JSONField(name = "terminals_id")
        private Integer terminals_id;
    }
}
