package com.eshore.feign.yinzhu;

import com.eshore.feign.yinzhu.req.*;
import com.eshore.feign.yinzhu.resp.*;

public interface YinZhuService {


    /**
    * 获取token
    */
    String getToken();


    /**
    * 获取终端信息
    */
    GetTerminalInfoResp getTerminalInfo();

    /**
     * 2-2. 获取正在执行的任务信息
     */
    ExecutingTaskResp getExecutingTaskInfo();

    /**
     * 2-3. 获取所有终端分组
     */
    GetGroupResp getTerminalsGroups();

    /**
    * 2-4. 获取服务器音频列表
    */
    GetServerMediaListResp getServerMediaList();

    /**
     * 2-5. 获取服务器TTS语音引擎
     */
    GetEngineInfoResp getEngineInfo();

    /**
     * 2-7. 获取远程播放任务状态
     */
    GetTaskInfoResp getTaskInfo(TaskReq req);

    /**
    * 3-3. 发起TTS播放(文本播放)
    */
    MP3PlayResp ttsMp3Play(TTSMP3PlayReq req);

    /**
     * 3-2. 点播http流媒体音乐
     * @param req
     * @return
     */
    MP3PlayResp httpMusic(HttpMusicReq req);

    /**
     * 3-8. 停止任务
     * @param req
     * @return
     */
    BaseResp stopTask(TaskReq req);

    /**
     * 3-9. 远程播放任务控制
     * @param req
     * @return
     */
    BaseResp controlRemoteTask(ControlRemoteTaskReq req);

    /**
     * 3-6. 设置任务音量
     * @param req
     */
    BaseResp setTaskVolume(TaskVolumeReq req);

    /**
     * 创建分组
     * @param req
     */
    CreateGroupResp createGroup(CreateGroupReq req);

}
