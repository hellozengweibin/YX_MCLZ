package com.eshore.feign.yinzhu.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数	必选	类型	说明
 * EndPointsAdditionalProp	true	string	终端附加描述 无需理会
 * Volume	true	int	音量 0-100,默认0
 * PlayMode	true	string	播放模式
 * normal_mode常规播放(列表播放1次)、list_cycle_mode列表循环播放、random_mode随机模式
 * EndPointsList	true	string	终端附id列表 如"1,2,3,4"调用GetTerminalInfo 接口获取
 * TaskID	true	string	任务UUID 自行生成
 * TaskName	true	string	任务名称 自行生成
 * MediaStreamUrls true string 媒体http链接 多个URL之间通过"|"分隔
 * TaskPriority	true	int	任务优先级1-100,默认0
*/
@NoArgsConstructor
@Data
public class HttpMusicReq {
    @JSONField(name = "EndPointsAdditionalProp")
    private String EndPointsAdditionalProp;
    @JSONField(name = "Volume")
    private Integer Volume;
    @JSONField(name = "PlayMode")
    private String PlayMode;
    @JSONField(name = "EndPointsList")
    private String EndPointsList;
    @JSONField(name = "TaskID")
    private String TaskID;
    @JSONField(name = "TaskName")
    private String TaskName;
    @JSONField(name = "MediaStreamUrls")
    private String MediaStreamUrls;
    @JSONField(name = "TaskPriority")
    private Integer TaskPriority;
}
