//package com.eshore.feign.weifengDianXiang.impl;
//
//import com.eshore.feign.weifengDianXiang.WeiFengService;
//import com.eshore.feign.weifengDianXiang.req.CameraSelectReq;
//import com.eshore.feign.weifengDianXiang.req.ControlDeviceReq;
//import com.eshore.feign.weifengDianXiang.req.DeviceSelectReq;
//import com.eshore.feign.weifengDianXiang.resp.AlarmArgsSelectResp;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class WeiFengServiceImplTest {
//
//
//    @Autowired
//    private WeiFengService weiFengService;
//    @Test
//    void deviceSelect() {
//
//        DeviceSelectReq req = new DeviceSelectReq();
//        req.setCurrentPage(1);
//        req.setPageSize(10);
//
//        weiFengService.deviceSelect(req);
//    }
//
//    @Test
//    void deviceRealStatus(){
//        weiFengService.deviceRealStatus("10000001");
//    }
//
//    @Test
//    void controlDevice(){
//        ControlDeviceReq req = new ControlDeviceReq();
//        req.setFilllightstatus(0);
//        req.setBoxarmingstatus(0);
//        req.setFlashlightstatus(1);
//        req.setHeaterstatus(0);
//        req.setCamerastatus(0);
//        req.setSwitchstatus(0);
//        req.setFanstatus(0);
//        req.setBuzzer2status(0);
//        req.setFwarning(0);
//        req.setPower12vO1(0);
//        req.setPower12vO2(0);
//
//
//        weiFengService.controlDevice("10000001",req);
//    }
//
//    @Test
//    void cameraList() {
//
//        CameraSelectReq req = new CameraSelectReq();
//        req.setCurrentPage(1);
//        req.setPageSize(10);
//        weiFengService.cameraSelect(req);
//    }
//
//    @Test
//    void alarmArgsSelect(){
//        weiFengService.alarmArgsSelect();
//    }
//
//    @Test
//    void alarmArgsUpdate(){
//        AlarmArgsSelectResp.DataDTO dataDTO = new AlarmArgsSelectResp.DataDTO();
//        dataDTO.setId("1");
//        dataDTO.setConfigName("CAMERA_ONLINE_INTERVAL");
//        dataDTO.setConfigValue("115");
//        dataDTO.setConfigRemark("摄像头轮询间隔时间(秒)");
//        weiFengService.alarmArgsUpdate(dataDTO);
//    }
//}
