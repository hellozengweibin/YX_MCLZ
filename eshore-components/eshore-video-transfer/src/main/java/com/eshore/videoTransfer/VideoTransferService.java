package com.eshore.videoTransfer;


import com.eshore.videoTransfer.req.PlaybackControlReq;
import com.eshore.videoTransfer.resp.GetPlayUrlResp;
import com.eshore.videoTransfer.resp.PlaybackControlResp;
import com.eshore.videoTransfer.req.*;
import com.eshore.videoTransfer.resp.BindDeviceListResp;
import com.eshore.videoTransfer.resp.GetRealPlayUrlResp;
import com.eshore.videoTransfer.resp.VideoConvertBaseResp;

/**
 * author:walker
 * time: 2022/10/13
 * description:  视频转换服务
 */
public interface VideoTransferService {


    /**
     * 绑定设备
     */
    VideoConvertBaseResp bindDevice(BindDeviceReq req);

    /**
     * 解绑设备
     */
    VideoConvertBaseResp unbindDevice(UnbindDeviceReq req);

    /**
     * 查询绑定设备列表
     */
    BindDeviceListResp bindDeviceList();

    /**
     * 上报设备状态
     */
    VideoConvertBaseResp upDeviceStatus(UpDeviceStatusReq req);

    /**
     * 获取实时视频流地址
     */
    GetRealPlayUrlResp getRealPlayUrl(GetRealPalyUrlReq req);


    /**
    * 下载录像
    */
    VideoConvertBaseResp downloadVideo(DownloadVideoReq req);


    /**
     * 获取媒体流地址
     * @param trVideoPlayParamVO
     * @return
     */
    GetPlayUrlResp getPlayUrl(TrVideoPlayParamVO trVideoPlayParamVO);

    /**
     * 录像回放控制
     * @param playbackControlReq
     * @return
     */
    PlaybackControlResp playbackControl(PlaybackControlReq playbackControlReq);



}
