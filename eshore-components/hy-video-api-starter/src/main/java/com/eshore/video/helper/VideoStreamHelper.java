package com.eshore.video.helper;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.exception.ServiceException;
import com.eshore.video.constants.CommConstants;
import com.eshore.video.constants.RedisConstants;
import com.eshore.video.constants.VideoApiConstants;
import com.eshore.video.domain.entity.InsightLoginBo;
import com.eshore.video.utils.UrlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component("videoStreamHelper")
@Slf4j
@Data
public class VideoStreamHelper {

    @Value("${cms.videoStreamUrl}")
    private String videoStreamUrl;

    @Value("${cms.playWay}")
    private String playWay;

    @Resource
    private RedisCache redisCache;





    /**
     * 通过慧眼视频登录数据获取慧眼视频连接内网地址
     *
     * @param areaCode      地区编码，如：4401
     * @return videoAddress 地址，格式：http://ip:port/
     */
    public String getVideoConnectAddress(String areaCode) {
        try {
            String videoConnectAddressKey = String.format(RedisConstants.VIDEO_CONNECT_ADDRESS_KEY, areaCode);
            if (redisCache.hasCacheKey(videoConnectAddressKey)) {
                return redisCache.getCacheObject(videoConnectAddressKey);
            }
            InsightLoginBo vo = getVideoLoginInfo(areaCode);
            if(vo == null){
                return null;
            }
            String loginUrl = vo.getLoginUrl(); // http://10.18.96.157:10081/loginV5
            String videoAddress = UrlUtil.getUrlRoot(loginUrl); // http://10.18.96.157:10081/
            log.info("==========> VideoStreamHelper getVideoConnectAddress: {}", videoAddress);
            // 缓存videoAddress
            redisCache.setCacheObject(videoConnectAddressKey, videoAddress, 24*60*60-300, TimeUnit.SECONDS);
            return videoAddress;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> VideoStreamHelper getVideoAccount error:[{}]", e);
        }
        return null;
    }

    /**
     * 通过慧眼视频登录数据获取慧眼视频账号
     *
     * @param areaCode      地区编码，如：4401
     * @return videoAccount
     */
    public String getVideoAccount(String areaCode) {
        try {
            String videoAccountKey = String.format(RedisConstants.VIDEO_ACCOUNT_KEY, areaCode);
            if (redisCache.hasCacheKey(videoAccountKey)) {
                return redisCache.getCacheObject(videoAccountKey);
            }
            InsightLoginBo vo = getVideoLoginInfo(areaCode);
            if(vo == null){
                return null;
            }
            String videoAccount = vo.getUserName();
            log.info("==========> VideoStreamHelper getVideoAccount: {}", videoAccount);
            // 缓存videoAccount
            redisCache.setCacheObject(videoAccountKey, videoAccount, 24*60*60-300, TimeUnit.SECONDS);
            return videoAccount;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> VideoStreamHelper getVideoAccount error:[{}]", e);
        }
        return null;
    }

    /**
     * 使用http-get获取慧眼视频登录数据
     *
     * @param areaCode      地区编码，如：4401
     * @return InsightLoginVo
     */
    public InsightLoginBo getVideoLoginInfo(String areaCode) {
        try {
            String url = videoStreamUrl + VideoApiConstants.VIDEO_ACCOUNT + "?areaCode="+ areaCode;
            log.info("==========> VideoStreamHelper getVideoLoginInfo: {}", url);
            String res = HttpUtil.get(url);
            String data = getResultData(res);
            InsightLoginBo vo = JSONObject.parseObject(data, InsightLoginBo.class);
            return vo;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> VideoStreamHelper getVideoLoginInfo error:[{}]", e);
        }
        return null;
    }

    /**
     * 使用http-get获取慧眼视频token
     *
     * @param areaCode      地区编码，如：4401
     * @return videoToken
     */
    public String getVideoToken(String areaCode) {
        try {
            String url = videoStreamUrl + VideoApiConstants.VIDEO_TOKEN + "?areaCode="+ areaCode;
            log.info("==========> VideoStreamHelper getVideoToken: {}", url);
            String res = HttpUtil.get(url);
            return getResultData(res);
        } catch (Exception e) {
            log.error(">>>>>>>>>>> VideoStreamHelper getVideoToken error:[{}]", e);
        }
        return null;
    }

    private String getResultData(String res){
        JSONObject jsonObject = JSONObject.parseObject(res);
        if(CommConstants.API.RESULT_CODE_SUCCESS != jsonObject.getIntValue("code")){
            log.error("============> VideoStreamHelper getResultData fail:[{}]", res);
            return null;
        }
        String data = jsonObject.getString("data");
        if (data == null) {
            log.error("============> VideoStreamHelper getResultData error");
            return null;
        }
        log.info("========> VideoStreamHelper getResultData success!", data);
        return data;
    }

}
