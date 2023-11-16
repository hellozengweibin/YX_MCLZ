package com.eshore.feign.yinzhu.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数	必选	类型	说明
 * TaskID	true	string	任务UUID
 * ControlCode	true	string	控制码
 * pause暂停播放,resume继续播放,previous_play上一曲,next_play下一曲 ,begin_play 开始播放
 * 当ControlCode值为play_index(指定序号歌曲播放),progress时为当前歌曲进度,值为play_mode时是具体播放模式
 * ControlValue  true string
*/
@NoArgsConstructor
@Data
public class ControlRemoteTaskReq {
    @JSONField(name = "TaskID")
    private String TaskID;
    @JSONField(name = "ControlCode")
    private String ControlCode;
    @JSONField(name = "ControlValue")
    private String ControlValue;
}
