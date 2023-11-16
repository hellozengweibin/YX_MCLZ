package com.eshore.feign.weifengDianXiang.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 参数名	解释
 * id	告警参数Id
 * configName	告警参数编码
 * configValue	告警参数值
 * configRemark	告警参数说明
 *
 * "id": 1,
 * "configName": "CAMERA_ONLINE_INTERVAL",
 * "configValue": "120",
 * "configRemark": "摄像头轮询间隔时间(秒)"
*/
@NoArgsConstructor
@Data
public class AlarmArgsSelectResp extends BaseResp{

    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String id;
        private String configName;
        private String configValue;
        private String configRemark;
    }
}
