package com.eshore.feign.weifengDianXiang;

import com.eshore.feign.weifengDianXiang.req.CameraSelectReq;
import com.eshore.feign.weifengDianXiang.req.ControlDeviceReq;
import com.eshore.feign.weifengDianXiang.req.DeviceSelectReq;
import com.eshore.feign.weifengDianXiang.resp.*;

import java.util.List;

/**
* author:walker
* time: 2023/4/25
* description:  微风电箱
*/
public interface WeiFengService {

    /**
    * 分页获取/筛选设备信息
     * @return
     */
    List<DeviceSelectResp> deviceSelect(DeviceSelectReq req);

    /**
    * 根据编号获取状态信息
    */
    DeviceRealStatusResp deviceRealStatus(String deviceNum);

    /**
    * 控制设备
    */
    BaseResp controlDevice(String deviceNum, ControlDeviceReq req);

    /**
    * 获取摄像机列表
    */
    List<CameraSelectResp> cameraSelect(CameraSelectReq req);

    /**
     * 告警参数查询
     */
    AlarmArgsSelectResp alarmArgsSelect();

    /**
     * 告警参数修改
     */
    BaseResp alarmArgsUpdate(AlarmArgsSelectResp.DataDTO dataDTO);

}
