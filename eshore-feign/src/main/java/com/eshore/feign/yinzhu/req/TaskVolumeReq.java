package com.eshore.feign.yinzhu.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数	必选	类型	说明
 * TaskID	true	string	任务UUID
 * Volume	true	int	 音量 0-100
*/
@NoArgsConstructor
@Data
public class TaskVolumeReq {
    @JSONField(name = "TaskID")
    private String TaskID;
    @JSONField(name = "Volume")
    private Integer Volume;
}
