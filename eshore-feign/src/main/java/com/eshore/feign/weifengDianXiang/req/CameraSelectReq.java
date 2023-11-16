package com.eshore.feign.weifengDianXiang.req;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
* 参数名	解释	类型	是否必须
 * currentPage	当前页码	Integer	是(>=1)
 * pageSize	单页大小	Integer	否(default=30)
 * bindDeviceNumber	所绑定的终端编号	String	否
 * deviceIP	设备IP	String	否
 * deviceName	设备名称	String	否
 * deviceStatus	限制条件，在线状态，1在线，0掉线	Integer	否
 * alarmStatus	限制条件，是否有告警，1有告警，0无告警	Integer	否
*/
@NoArgsConstructor
@Data
public class CameraSelectReq {
    private Integer currentPage;
    private Integer pageSize;
    private String bindDeviceNumber;
    private String deviceIP;
    private String deviceName;
    private Integer deviceStatus;
    private Integer alarmStatus;
}
