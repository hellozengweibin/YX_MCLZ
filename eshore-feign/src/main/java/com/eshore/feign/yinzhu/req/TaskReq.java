package com.eshore.feign.yinzhu.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数	必选	类型	说明
 * TaskID	true	string	任务UUID
*/
@NoArgsConstructor
@Data
public class TaskReq {
    @JSONField(name = "TaskID")
    private String TaskID;
}
