package com.eshore.feign.weifengDianXiang.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
* 参数名	解释
 * id	主键id
 * deviceIP	设备IP
 * deviceName	设备名称
 * deviceNum	设备编号
 * deviceStatus	设备状态，1在线，0掉线
 * alarmStatus	告警状态，1有告警，0无告警
 * operationUnitId	运维单位id
 * officeName	运维单位名称
 * operationUser	运维人员名称
 * operationUserId	运维人员id
 * areaCode	区域编码
 * areaName	区域名称
 * lastOnlineTime	上次上线时间
*/
@NoArgsConstructor
@Data
public class DeviceSelectResp {
    private String deviceIP;
    private String officeName;
    private String delFlag;
    private String deviceName;
    private String operationUser;
    private Integer deviceStatus;
    private String deviceNum;
    private Integer alarmStatus;
    private Integer areaCode;
    private Integer operationUnitId;
    private Integer operationUserId;
    private String areaName;
    private String lastOnlineTime;
    private Integer id;
}
