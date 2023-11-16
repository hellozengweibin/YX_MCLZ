package com.eshore.feign.weifengDianXiang.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数名	解释	类型	是否必须
 * filllightstatus	补光灯状态，0关闭，1打开	Integer	是
 * boxarmingstatus	箱体布防状态（同上）	Integer	是
 * flashlightstatus	照明灯01状态（同上）	Integer	是
 * heaterstatus	加热器状态（同上）	Integer	是
 * camerastatus	摄像机状态（同上）	Integer	是
 * switchstatus	交换机状态（同上）	Integer	是
 * fanstatus	风扇状态（同上）	Integer	是
 * buzzer2status	蜂鸣器02状态（同上）	Integer	是
 * fwarning	F告警（同上）	Integer	是
 * power12vO1	12VO1（同上）	Integer	是
 * power12vO2	12VO2（同上）	Integer	是
*/
@NoArgsConstructor
@Data
public class ControlDeviceReq {
    private Integer filllightstatus;
    private Integer boxarmingstatus;
    private Integer flashlightstatus;
    private Integer heaterstatus;
    private Integer camerastatus;
    private Integer switchstatus;
    private Integer fanstatus;
    private Integer buzzer2status;
    private Integer fwarning;
    private Integer power12vO1;
    private Integer power12vO2;
}
