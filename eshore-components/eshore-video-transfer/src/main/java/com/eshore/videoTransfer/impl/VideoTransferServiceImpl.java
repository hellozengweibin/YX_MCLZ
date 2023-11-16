package com.eshore.videoTransfer.impl;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.Assert;
import com.eshore.videoTransfer.req.PlaybackControlReq;
import com.eshore.videoTransfer.resp.GetPlayUrlResp;
import com.eshore.videoTransfer.resp.PlaybackControlResp;
import com.eshore.videoTransfer.VideoTransferService;
import com.eshore.videoTransfer.constant.VideoTransferConstant;
import com.eshore.videoTransfer.req.*;
import com.eshore.videoTransfer.resp.BindDeviceListResp;
import com.eshore.videoTransfer.resp.GetRealPlayUrlResp;
import com.eshore.videoTransfer.resp.VideoConvertBaseResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VideoTransferServiceImpl implements VideoTransferService {

    @Value("${video_transfer.baseUrl}")
    private String baseUrl;

    @Value("${video_transfer.timeOut:5000}")
    private int timeOut;


    @Value("${video_transfer.videoUrl:''}")
    private String videoUrl;

    @Value("${video_transfer.outputVideoUrl:''}")
    private String outputVideoUrl;

    interface URL {
        //绑定设备
        String BIND_DEVICE = "/api/v1/device/bind";
        //解绑设备
        String UNBIND_DEVICE = "/api/v1/device/unbind";
        //查询绑定设备列表
        String BINDLIST_DEVICE = "/api/v1/device/bindList";
        //上报设备状态
        String UP_DEVICE_STATUS = "/api/v1/device/status";
        //获取实时视频播放URL
        String GET_REALPLAY_URL = "/api/v1/play/getRealPlayUrl";
        //下载录像
        String DOWNLOAD_VIDEO = "/api/v1/download/device";

        String GET_LIVESTREAM_URL = "/api/v1/liveStream";
        String PLAYBACKCONTROL_URL = "/api/v1/playbackControl";
    }

    @Override
    public VideoConvertBaseResp bindDevice(BindDeviceReq req) {
        return sendPost(req, URL.BIND_DEVICE, VideoConvertBaseResp.class);
    }

    @Override
    public VideoConvertBaseResp unbindDevice(UnbindDeviceReq req) {
        return sendPost(req, URL.UNBIND_DEVICE, VideoConvertBaseResp.class);
    }

    @Override
    public BindDeviceListResp bindDeviceList() {
        return sendGet(URL.BINDLIST_DEVICE, BindDeviceListResp.class);
    }


    @Override
    public VideoConvertBaseResp upDeviceStatus(UpDeviceStatusReq req) {
        return sendPost(req, URL.UP_DEVICE_STATUS, VideoConvertBaseResp.class);
    }

    @Override
    public GetRealPlayUrlResp getRealPlayUrl(GetRealPalyUrlReq req) {
        GetRealPlayUrlResp resp = sendPost(req, URL.GET_REALPLAY_URL, GetRealPlayUrlResp.class);

        if (ObjectUtils.isNotEmpty(videoUrl) && ObjectUtils.isNotEmpty(outputVideoUrl) && ObjectUtils.isNotEmpty(resp)) {
            GetRealPlayUrlResp.ResultDTO resultDTO = resp.getResult();
            if (ObjectUtils.isNotEmpty(resultDTO)) {
                GetRealPlayUrlResp.ResultDTO.PlayUrlDTO playUrlDTO = resultDTO.getPlayUrl();
                String flvUrl = playUrlDTO.getFlvUrl();
                String rtmpUrl = playUrlDTO.getRtmpUrl();
                String hlsUrl = playUrlDTO.getHlsUrl();
                flvUrl = flvUrl.replace(videoUrl, outputVideoUrl);
                rtmpUrl = rtmpUrl.replace(videoUrl, outputVideoUrl);
                hlsUrl = hlsUrl.replace(videoUrl, outputVideoUrl);

                playUrlDTO.setFlvUrl(flvUrl);
                playUrlDTO.setRtmpUrl(rtmpUrl);
                playUrlDTO.setHlsUrl(hlsUrl);
            }
        }
        return resp;
    }

    @Override
    public VideoConvertBaseResp downloadVideo(DownloadVideoReq req) {
        return sendPost(req, URL.DOWNLOAD_VIDEO, VideoConvertBaseResp.class);
    }

    @Override
    public GetPlayUrlResp getPlayUrl(TrVideoPlayParamVO trVideoPlayParamVO) {
        return sendPost(trVideoPlayParamVO, URL.GET_LIVESTREAM_URL, GetPlayUrlResp.class);
    }

    @Override
    public PlaybackControlResp playbackControl(PlaybackControlReq playbackControlReq) {
        return sendPost(playbackControlReq, URL.PLAYBACKCONTROL_URL, PlaybackControlResp.class);
    }

    /**
     * 无参数的get请求
     */
    private <T> T sendGet(String detailUrl, Class<T> returnClaz) {
        try {

            String url = buildUrl(detailUrl);
            String res = HttpUtil.get(url, timeOut);
            log.info("==========> VideoTransferServiceImpl sendGet 请求url:{} 返回结果：{}", url, res);
            checkErr(res);
            return JSONObject.parseObject(res, returnClaz);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("==========> VideoTransferServiceImpl sendGet 请求异常，{}", e.getMessage());
            throw new ServiceException("baseUrl:" + baseUrl + e.getMessage());
        }

    }

    /**
     * json参数的post请求
     */
    private <T> T sendPost(Object req, String detailUrl, Class<T> tClass) {
        try {
            String url = buildUrl(detailUrl);
            String reqJson = JSONObject.toJSONString(req);
            String res = HttpUtil.post(url, reqJson, timeOut);
            log.info("==========> VideoTransferServiceImpl sendPost url:{} 请求参数：{} 返回结果：{}", url, reqJson, res);
            checkErr(res);
            return JSONObject.parseObject(res, tClass);
        } catch (Exception e) {
            log.error("==========> VideoTransferServiceImpl sendPost 请求异常，{}", e.getMessage());
            throw new ServiceException("baseUrl:" + baseUrl + e.getMessage());
        }

    }

    private void checkErr(String res) {
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (jsonObject == null) {
            Assert.throwMsg("调用接口失败");
        }
        if(jsonObject.containsKey(VideoTransferConstant.FIELD_CODE)) {
            if (!jsonObject.getInteger(VideoTransferConstant.FIELD_CODE).equals(VideoTransferConstant.SUCCESS)) {
                String reason = jsonObject.getString(VideoTransferConstant.FIELD_MSG);
                Assert.throwMsg("调用接口失败,原因为：" + reason);
            }
        }
        if(jsonObject.containsKey(VideoTransferConstant.RESULT_CODE)) {
            if (!jsonObject.getInteger(VideoTransferConstant.RESULT_CODE).equals(VideoTransferConstant.RESULT_SUCCESS)) {
                String reason = jsonObject.getString(VideoTransferConstant.RESULT_MSG);
                Assert.throwMsg("调用接口失败,原因为：" + reason);
            }
        }
    }


    private String buildUrl(String url) {
        return baseUrl + url;
    }

}
