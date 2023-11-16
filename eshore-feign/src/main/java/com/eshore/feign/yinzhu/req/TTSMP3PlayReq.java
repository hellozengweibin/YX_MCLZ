package com.eshore.feign.yinzhu.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数	必选	类型	说明
 * EndPointsAdditionalProp	true	string	终端附加描述 无需理会
 * EndPointsList	true	string	终端附id列表 调用[2-1获取]
 * TTSEngineName	true	string	TTS引擎名称, 通过接口查询获得
 * 请查看该文档中的 [调用2-5TSS引擎接口]
 * TTSSpeed	true	int	语速 1-10 (慢<5,5-正常,>5快)
 * RepeatTime	true	int	重复次数
 * TaskID	true	string	任务UUID 自行生成
 * TaskName	true	string	任务名称 自行生成
 * TaskPriority	true	int	任务优先级1-100,默认0
 * TextContent	true	string	要播放的文本内容
 * Volume	true	int	音量 0-100,默认0
*/
@NoArgsConstructor
@Data
public class TTSMP3PlayReq {
    @JSONField(name = "EndPointsAdditionalProp")
    private String EndPointsAdditionalProp;
    @JSONField(name = "EndPointsList")
    private String EndPointsList;
    @JSONField(name = "TTSEngineName")
    private String TTSEngineName;
    @JSONField(name = "TTSSpeed")
    private Integer TTSSpeed;
    @JSONField(name = "RepeatTime")
    private Integer RepeatTime;
    @JSONField(name = "TaskID")
    private String TaskID;
    @JSONField(name = "TaskName")
    private String TaskName;
    @JSONField(name = "TaskPriority")
    private Integer TaskPriority;
    @JSONField(name = "TextContent")
    private String TextContent;
    @JSONField(name = "Volume")
    private Integer Volume;
}
