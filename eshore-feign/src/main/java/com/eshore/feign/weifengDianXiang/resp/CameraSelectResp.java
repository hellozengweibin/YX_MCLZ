package com.eshore.feign.weifengDianXiang.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CameraSelectResp {
    private String deviceIP;
    private String officeName;
    private String delFlag;
    private String deviceName;
    private Integer areacode;
    private String deviceId;
    private String operationUser;
    private Integer deviceStatus;
    private String terminalName;
    private Integer operationUnitId;
    private Boolean alarmStatus;
    private String operationUserId;
    private String areaName;
    private String lastOnlineTime;
    private Integer id;
}
