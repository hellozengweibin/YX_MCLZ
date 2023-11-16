package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* data	object	返回数据 详细请看下面 '请求例子' 的 '正常返回'
 * company	string	公司名 无需理会
 * return_message	string	返回说明 默认空
 * result	int	200为请求成功
 * actioncode	string	操作码 正常值为 ls2c_get_server_terminals_status
 * token	string	token
 * sign	string	随机字符串 也可空
 *
 * "EndpointID": 终端ID,唯一标识(**重要**)
*/
@NoArgsConstructor
@Data
public class GetTerminalInfoResp extends BaseResp{
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private List<EndPointsArrayDTO> endPointsArray;

        @NoArgsConstructor
        @Data
        public static class EndPointsArrayDTO {
            private Integer alarmChannel;
            private Integer brightness;
            private Integer callCode;
            private Integer disableFlag;
            private Integer eq0;
            private Integer eq1;
            private Integer eq2;
            private Integer eq3;
            private Integer eq4;
            private Integer eightZone;
            private Integer enableFlag;
            private Integer endpointID;
            private String endpointIP;
            private String endpointMac;
            private String endpointName;
            private Integer endpointType;
            private String endpointTypeName;
            private String endpointVersion;
            private Integer lightModeID;
            private PositionDTO position;
            private Integer powerControl;
            private Integer proxyServerID;
            private String proxyServerIP;
            private String proxyServerName;
            private Integer shortOutput;
            private String status;
            private String statusDsp;
            private Integer surrportLED;
            private String taskID;
            private String taskName;
            private Integer taskType;
            private String taskTypeName;
            private Integer timeMode;
            private Integer volume;

            @NoArgsConstructor
            @Data
            public static class PositionDTO {
            }
        }
    }
}
