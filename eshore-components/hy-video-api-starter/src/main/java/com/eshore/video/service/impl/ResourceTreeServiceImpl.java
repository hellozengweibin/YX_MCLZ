package com.eshore.video.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eshore.common.utils.AesUtil;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.video.constants.CommConstants;
import com.eshore.video.constants.CommonResult;
import com.eshore.video.constants.VideoApiConstants;
import com.eshore.video.domain.entity.SearchVideoDto;
import com.eshore.video.domain.vo.CmsResponseVo;
import com.eshore.video.domain.vo.GetPlaybackUrlDto;
import com.eshore.video.domain.vo.GetTimeSliceVo;
import com.eshore.video.domain.vo.PTZHandlerDto;
import com.eshore.video.helper.VideoStreamHelper;
import com.eshore.video.helper.VideoTokenHelper;
import com.eshore.video.helper.VideoUrlHelper;
import com.eshore.video.service.ResourceTreeService;
import com.eshore.video.utils.DeviceGbUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author eshore
 * @date 2022-06-15
 */
@Service
public class ResourceTreeServiceImpl implements ResourceTreeService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceTreeServiceImpl.class);

    @Value("${cms.playWay}")
    private String playWay;

    @Value("${cms.ptzIp}")
    private String ptzIp;

    @Resource
    private VideoTokenHelper videoTokenHelper;


    @Autowired
    private VideoUrlHelper videoUrlHelper;


    @Resource
    private VideoStreamHelper videoStreamHelper;


    /**
     * 根层级ID
     */
    private static final long rootDeptId = 100L;

    @Override
    public CommonResult<JSONObject> getChannelList(String areaCode, String token) throws Exception {
        String videoAccount = videoStreamHelper.getVideoAccount(areaCode);
        if (StringUtils.isBlank(videoAccount)) {
            logger.info("----------------> getChannelList 未查询到该区域[{}]视频账号！", areaCode);
            return CommonResult.error();
        }

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json;");
        JSONObject req = new JSONObject();
        req.put("token", token);
        req.put("userName", videoAccount);

        // 2023/1/31 修改：不进行加解密
        Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
        String paramsStr = AesUtil.encrypt(instance, req.toJSONString());
        String url = videoUrlHelper.getCmsIpByAreaCode(areaCode) + "getChannelList";
        String result = HttpUtil.post(url, paramsStr);
        String desEncrypt = AesUtil.decrypt(result);

        JSONObject res = JSON.parseObject(desEncrypt);
        logger.info("----------------> getChannelList url：{}， param：{} result:{}", url, req.toJSONString(), result);
        return CommonResult.success(res);
    }


    /**
     * 获取实时推流链接
     *
     * @param deviceId 国标编码
     * @return
     */
    @Override
    public CommonResult getRealPlayUrl(String deviceId) {
        // todo 查询设备设备的类型,待确认

        return getRealPlayUrl(deviceId, "1");

    }


    @Override
    public CommonResult getRealPlayUrl(String deviceId, String type) {
        try {
            String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(deviceId);
            String token = videoTokenHelper.getTokenByAreaCode(areaCode);
            HashMap<String, Object> params = Maps.newHashMap();
            params.put("type", Integer.valueOf(type));
            params.put("deviceId", deviceId);
            params.put("token", token);
            int netTypeIntNum = videoUrlHelper.getNetTypeByAreaCode(areaCode);
            params.put("netType", netTypeIntNum);
            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String paramsStr = AesUtil.encrypt(instance, JSON.toJSONString(params));
            // 测试时，需检查对应的设备是否在对应的慧眼环境上
            String urlPrefix = videoUrlHelper.getCmsIpByAreaCode(areaCode);
            String url = urlPrefix + VideoApiConstants.REAL_PLAY;
            logger.info("请求的路径为：{},请求的参数为：{}", url, params);
//            String result = HttpUtil.post(url, paramsStr);
            String result = HttpRequest.post(url).header("Req-Net-Type", "http").timeout(10000).body(paramsStr).execute().body();
            return CommonResult.success(getPlayUrl(getPlayBackResult(result)));
        } catch (Exception e) {
            throw new RuntimeException("调用获取实时推流链接接口失败, " + e.getMessage());
        }
    }

    /**
     * 获取实时流播放地址(非登录)
     *
     * @param deviceId 国标编码
     * @return
     */
    @Override
    public String getRealPlayUrlByTimer(String deviceId) {
        try {
            String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(deviceId);
            String token = videoTokenHelper.getTokenByAreaCode(areaCode);
            HashMap<String, Object> params = Maps.newHashMap();
            params.put("type", 1);
            params.put("deviceId", deviceId);
            params.put("token", token);
            int netTypeIntNum = videoUrlHelper.getNetTypeByAreaCode(areaCode);
            params.put("netType", netTypeIntNum);
            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String paramsStr = AesUtil.encrypt(instance, JSON.toJSONString(params));
            String urlPrefix = videoUrlHelper.getCmsIpByAreaCode(areaCode);
//            String result = HttpUtil.post(urlPrefix + VideoApiConstants.REAL_PLAY, paramsStr);
            String result = HttpRequest.post(urlPrefix + VideoApiConstants.REAL_PLAY).header("Req-Net-Type", "http").timeout(10000).body(paramsStr).execute().body();
            return getPlayUrl(getPlayBackResult(result));
        } catch (Exception e) {
            throw new RuntimeException("调用获取实时推流链接接口失败, " + e.getMessage());
        }
    }

    public String getPlayUrl(JSONObject jsonObject) {
        return jsonObject.getJSONObject("playUrl").get(playWay + "Url").toString();
    }

    public String getCtrlUrl(JSONObject jsonObject) {
        return jsonObject.getString("ctrlUrl");
    }

    /**
     * 获取结果播放路径，辅助方法
     *
     * @param result
     * @return
     */
    private JSONObject getPlayBackResult(String result) throws Exception {
        String desEncrypt = AesUtil.decrypt(result);
        JSONObject res = JSON.parseObject(desEncrypt);
        logger.info("返回值{}", res.getString("resultCode"));
        if (res == null) {
            throw new Exception("调用获取实时推流链接接口失败, 响应的res为空");
        }
        if (!"0".equals(res.getString("resultCode"))) {
            throw new Exception(CommConstants.MSG_VER.formulaMap.get(res.getString("resultCode")));
        }
        if (ObjectUtils.isEmpty(res.getJSONObject("data")) || ObjectUtils.isEmpty(res.getJSONObject("data").getJSONObject("playUrl"))
                || ObjectUtils.isEmpty(res.getJSONObject("data").getJSONObject("playUrl").get(playWay + "Url"))) {
            throw new Exception(CommConstants.MSG_VER.formulaMap.get(CommConstants.API.RESULT_CODE_EMPTY));
        }
        JSONObject data = res.getJSONObject("data");
        return data;
    }

    private String getResultCode(String result) throws Exception {
        String desEncrypt = AesUtil.decrypt(result);
        JSONObject res = JSON.parseObject(desEncrypt);
        logger.info("返回值{}", res.getString("resultCode"));
        if (!"0".equals(res.getString("resultCode"))) {
            throw new Exception(CommConstants.MSG_VER.formulaMap.get(res.getString("resultCode")));
        }
        return res.getString("resultCode");
    }

    /**
     * 获取回放推流链接
     *
     * @param deviceId  国标编码
     * @param startTime 时间格式: yyyyMMddHHmmss
     * @param endTime   时间格式: yyyyMMddHHmmss
     * @return
     */
    @Override
    public JSONObject getPlaybackUrl(String deviceId, String startTime, String endTime) {
        return getPlaybackUrl(deviceId, startTime, endTime, true, 1);
    }

    /**
     * @param deviceId
     * @param startTime
     * @param endTime
     * @param getTimeSlice 和 storageType 是一体的，用来判断慧眼的存储地址
     * @param storageType  getTimeSlice为false的时候前端将storegeType传过来
     * @return
     */
    public JSONObject getPlaybackUrl(String deviceId, String startTime, String endTime, Boolean getTimeSlice, Integer storageType) {
        try {
            if (getTimeSlice == true) {
                String timeSlice = getTimeSlice(deviceId, DateUtil.parse(startTime).toString(DatePattern.PURE_DATE_FORMAT), 1);
                storageType = StrUtil.isBlank(timeSlice) ? 0 : 1;
            }
            String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(deviceId);
            String token = videoTokenHelper.getTokenByAreaCode(areaCode);
            HashMap<String, Object> params = Maps.newHashMap();
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            params.put("deviceId", deviceId);
            params.put("token", token);
            params.put("storageType", storageType);
            int netTypeIntNum = videoUrlHelper.getNetTypeByAreaCode(areaCode);
            params.put("netType", netTypeIntNum);
            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String paramsStr = AesUtil.encrypt(instance, JSON.toJSONString(params));
            String result = HttpUtil.post(videoUrlHelper.getCmsIpByAreaCode(areaCode) + VideoApiConstants.PLAY_BACK, paramsStr);
            return getPlayBackResult(result);
        } catch (Exception e) {
            throw new RuntimeException("调用获取回放推流链接接口失败, " + e.getMessage());
        }
    }

    /**
     * 下载视频
     *
     * @param deviceId
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @Override
    public String downloadVideo(String deviceId, String startTime, String endTime) throws Exception {
        String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(deviceId);
        // 获取token
        String token = videoTokenHelper.getTokenByAreaCode(areaCode);
        String cmsIp = videoUrlHelper.getCmsIpByAreaCode(areaCode);
        if (StringUtils.isEmpty(token)) {
            throw new Exception("获取token失败");
        }
        // 校验是否存在该录像 todo 优化
        HashMap<String, Object> map = new HashMap<>();
        map.put("deviceID", deviceId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("token", token);
        String ifExistPath = "";
        // 校验环境使用是内网环境还是外网
        if (videoUrlHelper.getNetTypeByAreaCode(areaCode).equals(0)) {
            ifExistPath = cmsIp + VideoApiConstants.RECORDQUERY;
        } else {
            ifExistPath = cmsIp + VideoApiConstants.RECORDQUERY;
        }
        // 加密
        try {
            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String encrypt = AesUtil.encrypt(instance, JSON.toJSONString(map));
            String result = HttpUtil.post(ifExistPath, encrypt);
            String desEncrypt = AesUtil.decrypt(result);
            JSONObject res = JSON.parseObject(desEncrypt);
            logger.info("检索访问路径：{},调用慧眼接口返回的状态码：{}", ifExistPath, res.getString("resultCode"));
            if (!"0".equals(res.getString("resultCode"))) {
                throw new Exception("该视频无法下载");
            }
        } catch (Exception e) {
            logger.error("解密响应消息异常:", e);
        }
        // 下载路径： 记得区分慧眼的内网和外网地址是否相同，否则报404
        String downPath = VideoApiConstants.DOWNLOAD_URL + "/" + deviceId + "/" + startTime + ":" + endTime + "/";
        // 校验环境使用是内网环境还是外网
        if (videoUrlHelper.getNetTypeByAreaCode(areaCode).equals(0)) {
            logger.info("内网 —— 拼接下载请求路径成功：" + cmsIp + downPath + token);
            return cmsIp + downPath + token;
        }
        String cmsForOutIp = videoUrlHelper.getForOutIpByAreaCode(areaCode);
        logger.info("外网 —— 拼接下载请求路径成功：" + cmsForOutIp + downPath + token);
        return cmsForOutIp + downPath + token;
    }

    @Override
    public CommonResult getTimeSlice(String deviceId, String time) {
        GetTimeSliceVo vo = new GetTimeSliceVo();
        List<String[]> abList = new ArrayList<>();
        int storageType = 0;
        try {
            String str = getTimeSlice(deviceId, time, storageType);
            if (StrUtil.isBlank(str)) {
                storageType = 1;
                str = getTimeSlice(deviceId, time, storageType);
            }
            if (StrUtil.isBlank(str)) {
                return CommonResult.error("获取回放记录失败");
            }
            JSONArray jarr = JSON.parseArray(str);
            for (int i = 0; i < jarr.size(); i++) {
                String[] abStr = new String[2];
                String stime = JSON.parseObject(jarr.getString(i)).getString("StartTime");
                String etime = JSON.parseObject(jarr.getString(i)).getString("EndTime");
                int aHour = LocalDateTime.parse(stime).getHour();
                int aMin = LocalDateTime.parse(stime).getMinute();
                int aSec = LocalDateTime.parse(stime).getSecond();
                int bHour = LocalDateTime.parse(etime).getHour();
                int bMin = LocalDateTime.parse(etime).getMinute();
                //播放无法控制到秒，只能舍弃无法渲染的部分。去开始时间最大的分钟，以及结束时间
                if (aSec > 0) {
                    aMin = aMin + 1;
                }
                abStr[0] = setZeroForInt(aHour) + ":" + setZeroForInt(aMin);
                abStr[1] = setZeroForInt(bHour) + ":" + setZeroForInt(bMin);
                abList.add(abStr);
            }
            vo.setTimeBar(abList);
            vo.setStorageType(storageType);
        } catch (Exception e) {
            return CommonResult.error("-1", e.getMessage());
        }
        return CommonResult.success(vo);
    }

    private String getTimeSlice(String deviceId, String time, int storageType) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(deviceId);
        String token = videoTokenHelper.getTokenByAreaCode(areaCode);
        params.put("deviceID", deviceId);
        params.put("startTime", time + "000000");
        params.put("endTime", time + "235959");
        params.put("token", token);
        params.put("storageType", storageType);
        try {
            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String paramsStr = AesUtil.encrypt(instance, JSON.toJSONString(params));
            String result = HttpUtil.post(videoUrlHelper.getCmsIpByAreaCode(areaCode) + "api/v2/recordqueryV1", paramsStr);
            String desEncrypt = AesUtil.decrypt(result);
            logger.info("调用服务获取时间段接口, result: {}", desEncrypt);
            JSONObject ret = JSON.parseObject(desEncrypt);
            String code = ret.get("resultCode").toString();
            logger.info("调用服务获取时间段接口返回 code：{} , msg： {}", code, CommConstants.MSG_VER.formulaMap.get(code));
            //成功
            if (CommConstants.API.RESULT_CODE_SUC.equals(code)) {
                return ret.get("data").toString();
            } else {
                logger.error("获取回放记录失败，result: {}", desEncrypt);
            }
        } catch (Exception e) {
            logger.error("调用服务获取时间段接口异常, e：{}", e.getMessage());
        }
        return null;
    }

    private String setZeroForInt(int num) {
        String numStr = num + "";
        if (num < 10) {
            numStr = "0" + num;
        }
        return numStr;
    }


    @Override
    public CommonResult<CmsResponseVo> ptzHandler(PTZHandlerDto ptzHandlerDto) {
        // 获取播放路径
        try {
            String token = videoTokenHelper.getVideoTokenByUserId(DeviceGbUtil.extractAreaCodeByDeviceId(ptzHandlerDto.getDeviceId()), SecurityUtils.getUserId());
            // 通过cmd参数判断控制参数是否正常
            if (checkYunTaiParam(ptzHandlerDto) == -1) {
                return CommonResult.error("请输入命令必填的正常参数");
            }

            HashMap<String, Object> ptzParams = Maps.newHashMap();
            // 请求参数 拼接播放控制参数
            ptzParams.put("device_id", ptzHandlerDto.getDeviceId());
            ptzParams.put("cmd", ptzHandlerDto.getCmd());
            // 播放速度参数
            ptzParams.put("speed", ptzHandlerDto.getSpeed());
            ptzParams.put("minspeed", ptzHandlerDto.getMinSpeed());
            ptzParams.put("maxspeed", ptzHandlerDto.getMaxSpeed());
            // 巡航参数
            ptzParams.put("preset_id", ptzHandlerDto.getPresetId());
            ptzParams.put("preset_tour_id", ptzHandlerDto.getPresetTourId());
            ptzParams.put("staytime", ptzHandlerDto.getStayTime());
            ptzParams.put("token", token);

            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String paramsStr = AesUtil.encrypt(instance, JSON.toJSONString(ptzParams));
            // todo
            logger.info("访问路径{}，访问参数{}", ptzIp, ptzParams);
            String result = HttpUtil.post(ptzIp, paramsStr);
            return CommonResult.success(getPlayBackResult(result));
        } catch (Exception e) {
            throw new RuntimeException("云台控制失败" + e.getMessage());
        }
    }

    @Override
    public CommonResult getPlayBackDownLoad(SearchVideoDto searchVideoDto) {
        String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(searchVideoDto.getDeviceId());
        String token = videoTokenHelper.getTokenByAreaCode(areaCode);
        String deviceId = searchVideoDto.getDeviceId();
        String start = searchVideoDto.getStart();
        String end = searchVideoDto.getEnd();
        // 下载路径： 记得区分慧眼的内网和外网地址是否相同，否则报404
        // 校验环境使用是内网环境还是外网
        String downloadPrefix = videoUrlHelper.getNetTypeByAreaCode(areaCode).equals(0)
                ? videoUrlHelper.getCmsIpByAreaCode(areaCode) : videoUrlHelper.getForOutIpByAreaCode(areaCode);
        String returnUrl = downloadPrefix + "alarmRecordplay" + "/" + deviceId + "/" + start + ":" + end + "/" + token + "/" + videoUrlHelper.getNetTypeByAreaCode(areaCode);
        logger.info("拼接下载请求路径成功：{}", returnUrl);
        return CommonResult.success(returnUrl);
    }

    @Override
    public CommonResult<CmsResponseVo> getPlaybackUrl(GetPlaybackUrlDto dto) {
        try {
            String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(dto.getDeviceId());
            String token = videoTokenHelper.getTokenByAreaCode(areaCode);
            HashMap<String, Object> params = Maps.newHashMap();
            params.put("startTime", dto.getStartTime());
            params.put("endTime", dto.getEndTime());
            params.put("deviceId", dto.getDeviceId());
            params.put("token", token);
            params.put("storageType", dto.getStorageType());
            params.put("netType", videoUrlHelper.getNetTypeByAreaCode(areaCode));
            Cipher instance = Cipher.getInstance(CommConstants.CIPHER_PADDING.PKCS5_PADDING);
            String paramsStr = AesUtil.encrypt(instance, JSON.toJSONString(params));
            String result = HttpUtil.post(videoUrlHelper.getCmsIpByAreaCode(areaCode) + VideoApiConstants.PLAY_BACK, paramsStr);
            return CommonResult.success(getPlayUrl(getPlayBackResult(result)));
        } catch (Exception e) {
            throw new RuntimeException("调用获取回放推流链接接口失败, " + e.getMessage());
        }
    }

    private int checkYunTaiParam(PTZHandlerDto dto) {
        String cmd = dto.getCmd();
        if (cmd.equals("5")) {
            return 0;
        } else if (cmd.equals("16") || cmd.equals("18")) {
            // 判断必填参数是否正常
            if (StringUtils.isEmpty(dto.getPresetId())) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (StringUtils.isEmpty(dto.getSpeed()) || StringUtils.isEmpty(dto.getMinSpeed()) || StringUtils.isEmpty(dto.getMaxSpeed())) {
                return -1;
            } else {
                return 1;
            }
        }
    }


    public static void main(String[] args) throws Exception {

    }

}
