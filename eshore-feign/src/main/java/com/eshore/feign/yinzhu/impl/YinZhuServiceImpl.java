package com.eshore.feign.yinzhu.impl;

import cn.hutool.core.util.StrUtil;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.utils.Assert;
import com.eshore.common.utils.http.HttpUtils;
import com.eshore.feign.yinzhu.YinZhuService;
import com.eshore.feign.yinzhu.constant.YinZhuConstants;
import com.eshore.feign.yinzhu.constant.YinZhuProperties;
import com.eshore.feign.yinzhu.req.*;
import com.eshore.feign.yinzhu.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.eshore.feign.yinzhu.impl.YinZhuServiceImpl.ActionCode.C2LS_GET_TTS_ENGINE_INFO;

@Service
public class YinZhuServiceImpl implements YinZhuService {

    interface URL{
        //获取token
        String GET_TOKEN="/api/v29+/auth?name=%s&password=%s&force=1";

        //forwarder
        String FORWARDER="/api/v29+/ws/forwarder";

        //创建分组
        String TERMINALS_GROUPS = "/api/v29+/terminals-groups";

        //获取分组
        String GET_TERMINALS_GROUPS = "/api/v29+/terminals-groups/all?withTerminals=true";
    }

    interface ActionCode{
        //获取终端
        String C2LS_GET_SERVER_TERMINALS_STATUS="c2ls_get_server_terminals_status";

        //获取媒体列表
        String C2LS_GET_SERVER_MUSIC_LIST="c2ls_get_server_music_list";

        //获取正在执行的任务信息
        String C2LS_GET_TASK__STATUS="c2ls_get_task_status";

        //获取远程播放任务状态
        String C2LS_GET_TASK_PLAY_STATUS="c2ls_get_task_play_status";

        //获取引擎
        String C2LS_GET_TTS_ENGINE_INFO="c2ls_get_tts_engine_info";

        //发起TTS播放(文本播放)
        String C2LS_SERVER_TTS_MP3_PLAY="c2ls_server_tts_mp3_play";

        //点播http流媒体音乐
        String C2LS_DAMAND_MEDIA_STREAM="c2ls_damand_media_stream";

        //停止任务
        String C2LS_STOP_TASK="c2ls_stop_task";

        //远程播放任务控制
        String C2LS_CONTROL_REMOTE_TASK="c2ls_control_remote_task";

        //设置任务音量
        String C2LS_SET_TASK_VOLUME="c2ls_set_task_volume";

    }

    @Autowired
    private YinZhuProperties yinZhuProperties;
    @Autowired
    private RedisCache redisCache;



    @Override
    public String getToken() {
        String token = redisCache.getCacheObject(YinZhuConstants.Redis.TOKEN);
        if(!StrUtil.isEmpty(token)){
            System.out.println(token);
            return token;
        }

        GetTokenResp resp = HttpUtils.get(yinZhuProperties.getUrl(), URL.GET_TOKEN, GetTokenResp.class, yinZhuProperties.getUserName(), yinZhuProperties.getPsd());
        if(resp==null||!resp.getResult().equals(YinZhuConstants.SUCCESS_CODE)){
            Assert.throwMsg("调用接口失败");
        }
        token = resp.getToken();
        //token的最长有效时间为120分钟,建议60分钟刷新一次
        redisCache.setCacheObject(YinZhuConstants.Redis.TOKEN,token,1, TimeUnit.HOURS);
        System.out.println(token);
        return token;
    }

    @Override
    public GetTerminalInfoResp getTerminalInfo() {
        return putAction(null,ActionCode.C2LS_GET_SERVER_TERMINALS_STATUS,GetTerminalInfoResp.class);
    }

    @Override
    public ExecutingTaskResp getExecutingTaskInfo() {
        return putAction(null,ActionCode.C2LS_GET_TASK__STATUS,ExecutingTaskResp.class);
    }

    @Override
    public GetGroupResp getTerminalsGroups() {
        return HttpUtils.getWithHeader(yinZhuProperties.getUrl(), URL.GET_TERMINALS_GROUPS,null,GetGroupResp.class,getToken());
    }

    @Override
    public GetServerMediaListResp getServerMediaList() {
        return putAction(null,ActionCode.C2LS_GET_SERVER_MUSIC_LIST,GetServerMediaListResp.class);
    }

    @Override
    public GetEngineInfoResp getEngineInfo() {
        return putAction(null,C2LS_GET_TTS_ENGINE_INFO,GetEngineInfoResp.class);
    }

    @Override
    public GetTaskInfoResp getTaskInfo(TaskReq req) {
        return putAction(req,ActionCode.C2LS_GET_TASK_PLAY_STATUS,GetTaskInfoResp.class);
    }

    @Override
    public MP3PlayResp ttsMp3Play(TTSMP3PlayReq req) {


        return putAction(req,ActionCode.C2LS_SERVER_TTS_MP3_PLAY, MP3PlayResp.class);
    }

    @Override
    public MP3PlayResp httpMusic(HttpMusicReq req) {
        return putAction(req,ActionCode.C2LS_DAMAND_MEDIA_STREAM, MP3PlayResp.class);
    }

    @Override
    public BaseResp stopTask(TaskReq req) {
        return putAction(req,ActionCode.C2LS_STOP_TASK, BaseResp.class);
    }

    @Override
    public BaseResp controlRemoteTask(ControlRemoteTaskReq req) {
        return putAction(req,ActionCode.C2LS_CONTROL_REMOTE_TASK, BaseResp.class);
    }

    @Override
    public BaseResp setTaskVolume(TaskVolumeReq req) {
        return putAction(req,ActionCode.C2LS_SET_TASK_VOLUME, BaseResp.class);
    }

    @Override
    public CreateGroupResp createGroup(CreateGroupReq req) {
        return HttpUtils.postWithHeader(yinZhuProperties.getUrl(), URL.TERMINALS_GROUPS, req, CreateGroupResp.class,getToken());
    }


    /**
    * forwarder put方法
    */
    private <T> T putAction(Object data,String actioncode,Class<T> rClaz) {
        ForwarderReq req = new ForwarderReq();
        req.setActioncode(actioncode);
        req.setToken(getToken());
        req.setCompany("BL");
        if(data!=null){
            req.setData(data);
        }
        return HttpUtils.put(yinZhuProperties.getUrl(),URL.FORWARDER, req,rClaz);
    }


}
