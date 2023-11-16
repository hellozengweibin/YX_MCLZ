package com.eshore.feign.weifengDianXiang.impl;

import com.eshore.common.utils.Assert;
import com.eshore.common.utils.http.HttpUtils;
import com.eshore.feign.weifengDianXiang.WeiFengService;
import com.eshore.feign.weifengDianXiang.constants.WeiFengConstants;
import com.eshore.feign.weifengDianXiang.constants.WeifengProperties;
import com.eshore.feign.weifengDianXiang.req.CameraSelectReq;
import com.eshore.feign.weifengDianXiang.req.ControlDeviceReq;
import com.eshore.feign.weifengDianXiang.req.DeviceSelectReq;
import com.eshore.feign.weifengDianXiang.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eshore.feign.weifengDianXiang.impl.WeiFengServiceImpl.URL.DEVICE_SELECT;

@Service
public class WeiFengServiceImpl implements WeiFengService {

    @Autowired
    private WeifengProperties weifengProperties;

    interface URL{
        //设备查询
        String DEVICE_SELECT="/api/device/select";
        //设备状态
        String DEVICE_REALSTATUS="/api/devices/realStatus/%s";
        //控制设备
        String CONTROL_DEVICE="/api/devices/config/%s";
        //摄像头列表
        String CAMERA_SELECT="/api/camera/select";
        //查询告警参数
        String ALARM_ARGS_SELECT = "/api/camera/sysConfigList";
        //修改告警参数
        String ALARM_ARGS_UPDATE = "/api/camera/sysConfigSet";
    }

    @Override
    public List<DeviceSelectResp> deviceSelect(DeviceSelectReq req) {
        return sendPagePost(req,DEVICE_SELECT,DeviceSelectResp.class);
    }

    /**
    * 发送分页列表
    */
    private <R> List<R> sendPagePost(Object req,String suffix,Class<R> rClass) {
        BasePageResp page = HttpUtils.post(weifengProperties.getUrl(), suffix, req, BasePageResp.class);
        return getPageList(page,rClass);
    }


    private <R> List<R> getPageList(BasePageResp page,Class<R> rClass) {
        if(page ==null||!page.getCode().equals(WeiFengConstants.SUCCESS_CODE)|| page.getData()==null){
            Assert.throwMsg("请求url：%s失败",weifengProperties.getUrl()+DEVICE_SELECT);
        }
        BasePageResp.DataDTO<R> data = page.getData();
        return data.getList();
    }

    @Override
    public DeviceRealStatusResp deviceRealStatus(String deviceNum) {
        return HttpUtils.get(weifengProperties.getUrl(),URL.DEVICE_REALSTATUS,DeviceRealStatusResp.class,deviceNum);
    }

    @Override
    public BaseResp controlDevice(String deviceNum, ControlDeviceReq req) {
        return HttpUtils.post(weifengProperties.getUrl(),URL.CONTROL_DEVICE,req,BaseResp.class,deviceNum);
    }

    @Override
    public List<CameraSelectResp> cameraSelect(CameraSelectReq req) {
        return sendPagePost(req,URL.CAMERA_SELECT,CameraSelectResp.class);
    }

    @Override
    public AlarmArgsSelectResp alarmArgsSelect() {
        return HttpUtils.get(weifengProperties.getUrl(),URL.ALARM_ARGS_SELECT,AlarmArgsSelectResp.class,"");
    }

    @Override
    public BaseResp alarmArgsUpdate(AlarmArgsSelectResp.DataDTO dataDTO) {
        return HttpUtils.post(weifengProperties.getUrl(),URL.ALARM_ARGS_UPDATE,dataDTO,BaseResp.class,"");
    }


}
