package com.eshore.video.service;

import com.alibaba.fastjson.JSONObject;
import com.eshore.video.constants.CommonResult;
import com.eshore.video.domain.entity.SearchVideoDto;
import com.eshore.video.domain.vo.CmsResponseVo;
import com.eshore.video.domain.vo.GetPlaybackUrlDto;
import com.eshore.video.domain.vo.PTZHandlerDto;

/**
 * 资源组数据service
 *
 * @author xieyongjian
 * @date 2022-06-20
 */
public interface ResourceTreeService {

    /**
     * 获取实时流播放地址
     *
     * @param deviceId
     * @return
     */
    CommonResult getRealPlayUrl(String deviceId) throws Exception;

    CommonResult getRealPlayUrl(String deviceId, String type) throws Exception;

    /**
     * 获取实时流播放地址(非登录)
     *
     * @param deviceId
     * @return
     */
    String getRealPlayUrlByTimer(String deviceId) throws Exception;

    /**
     * 获取回看流播放地址
     *
     * @param deviceId
     * @return
     */
    JSONObject getPlaybackUrl(String deviceId, String startTime, String endTime) throws Exception;

    /**
     * 下载视频
     *
     * @param deviceId
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    String downloadVideo(String deviceId, String startTime, String endTime) throws Exception;

    /**
     * 获取时段视频
     *
     * @param deviceId 国标
     * @param time     时间
     * @return
     */
    CommonResult getTimeSlice(String deviceId, String time);

    /**
     * 获取平台账号下挂的设备列表
     *
     * @param userName 视频帐号
     * @param token    视频token
     * @return
     * @throws Exception
     */
    CommonResult<JSONObject> getChannelList(String userName, String token) throws Exception;









    CommonResult<CmsResponseVo> ptzHandler(PTZHandlerDto ptzHandlerDto);

    CommonResult getPlayBackDownLoad(SearchVideoDto searchVideoDto);

    CommonResult<CmsResponseVo> getPlaybackUrl(GetPlaybackUrlDto dto);


}
