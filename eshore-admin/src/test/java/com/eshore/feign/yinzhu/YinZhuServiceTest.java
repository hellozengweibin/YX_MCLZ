package com.eshore.feign.yinzhu;

import com.eshore.common.utils.DateUtils;
import com.eshore.feign.yinzhu.req.*;
import com.eshore.feign.yinzhu.resp.GetTaskInfoResp;
import com.eshore.feign.yinzhu.resp.MP3PlayResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class YinZhuServiceTest {

    @Autowired
    private YinZhuService yinZhuService;

    @Test
    void getToken() {
        yinZhuService.getToken();
    }

    @Test
    void getTerminalInfo(){
        yinZhuService.getTerminalInfo();
    }

    @Test
    void getMediaList(){
        yinZhuService.getServerMediaList();
    }

    @Test
    void GetEngineInfo(){
        yinZhuService.getEngineInfo();
    }

    @Test
    void mp3Send(){
        TTSMP3PlayReq req = new TTSMP3PlayReq();
        req.setEndPointsAdditionalProp("");
        req.setEndPointsList("1");
        req.setTTSEngineName("xiaoyan");
        req.setTTSSpeed(5);
        req.setRepeatTime(30);
        req.setTextContent("音柱测试音柱测试音柱测试音柱测试音柱测试音柱测试音柱测试音柱测试");
        req.setVolume(1);
        req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
        req.setTaskName("测试任务"+ DateUtils.dateTimeNow());
        req.setTaskPriority(70);
        yinZhuService.ttsMp3Play(req);
    }

    @Test
    void httpMusic(){
//        HttpMusicReq req = new HttpMusicReq();
//        req.setEndPointsAdditionalProp("");
//        req.setVolume(50);
//        req.setPlayMode("normal_mode");
//        req.setEndPointsList("1");
//        String TaskID = String.format("{CCC3D531AEDE4C5CAB4630C847655ACO%s}", 0);
////            req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
//        req.setTaskID(TaskID);
//        req.setTaskName("点播流媒体音乐"+ DateUtils.dateTimeNow());
//        req.setMediaStreamUrls("http://132.122.1.76:9000/sound/gird.mp3|http://132.122.1.76:9000/sound/wawa.mp3");
////        req.setMediaStreamUrls("http://132.122.1.76:9000/sound/gird.mp3");
//        req.setTaskPriority(70);
//        MP3PlayResp mp3PlayResp = yinZhuService.httpMusic(req);

        HttpMusicReq req1 = new HttpMusicReq();
        req1.setEndPointsAdditionalProp("");
        req1.setVolume(1);
        req1.setPlayMode("normal_mode");
        req1.setEndPointsList("1,2");
        String TaskID1 = String.format("{CCC3D531AEDE4C5CAB4630C847655ACO%s}", 1);
//            req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
        req1.setTaskID(TaskID1);
        req1.setTaskName("点播流媒体音乐"+ DateUtils.dateTimeNow());
//        req1.setMediaStreamUrls("http://132.122.1.76:9000/sound/gird.mp3|http://132.122.1.76:9000/sound/wawa.mp3");
        req1.setTaskPriority(70);
        req1.setMediaStreamUrls("http://132.122.1.76:9000/sound/gird.mp3");
        MP3PlayResp mp3PlayResp1 = yinZhuService.httpMusic(req1);
        System.out.println("======================"+mp3PlayResp1.getResult());

    }

    @Test
    void stopTask(){
        //可以停止服务器所有正在执行的任务
        TaskReq req = new TaskReq();
        req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
        yinZhuService.stopTask(req);
    }

    @Test
    void GetExecutingTaskInfo(){
        yinZhuService.getExecutingTaskInfo();
    }

    @Test
    void controlRemoteTask(){
        //只能控制远程播放的mp3任务,不能控制本地播放任务.
        ControlRemoteTaskReq req = new ControlRemoteTaskReq();
        req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
        req.setControlCode("pause");
//        req.setControlCode("resume");
//        req.setControlCode("previous_play");
//        req.setControlCode("next_play");
        req.setControlValue("");


//        req.setControlCode("play_index");
//        req.setControlValue("2");
//
//        req.setControlCode("progress");
//        req.setControlValue("80");
//
//        req.setControlCode("play_mode");
//        req.setControlValue("normal_mode");
//        req.setControlValue("list_cycle_mode");
//        req.setControlValue("random_mode");

        yinZhuService.controlRemoteTask(req);
    }

    @Test
    void taskInfo(){
        TaskReq req = new TaskReq();
        req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
        GetTaskInfoResp taskInfo = yinZhuService.getTaskInfo(req);
        System.out.println(taskInfo.getData().getPlayStatus());
    }

    @Test
    void taskVolume(){
        TaskVolumeReq req = new TaskVolumeReq();
        req.setTaskID("{CCC3D531AEDE4C5CAB4630C847655ACO1}");
        req.setVolume(40);
        yinZhuService.setTaskVolume(req);
    }

    @Test
    void createGroup(){
        CreateGroupReq req = new CreateGroupReq();
        req.setName("test");
        req.setCall_code(100);
        List<CreateGroupReq.TerminalsDto> dtos =  new ArrayList<>();
        CreateGroupReq.TerminalsDto terminalsDto = new CreateGroupReq.TerminalsDto();
        terminalsDto.setTerminals_id(1);
        dtos.add(terminalsDto);
        req.setTerminals(dtos);
        yinZhuService.createGroup(req);
    }

    @Test
    void GetTerminalsGroups(){yinZhuService.getTerminalsGroups();}

}
