package com.eshore.video.helper;

import com.eshore.common.constant.Constants;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.exception.ServiceException;
import com.eshore.video.utils.DeviceGbUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

@Component("videoTokenHelper")
@Slf4j
@Data
public class VideoTokenHelper {

    @Resource
    private RedisCache redisCache;

    @Resource
    private VideoStreamHelper videoStreamHelper;


    /**
     * 根据用户id和地市编号获取视频平台token
     *
     * @param userId
     * @param areaCode 地市编号
     * @return
     */
    public String getVideoTokenByUserId(String areaCode, Object userId) {
        Collection<String> loginTokens = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
        if (CollectionUtils.isEmpty(loginTokens)) {
            log.error("=======================>getVideoTokenByUserId =>userId: {} not exist", userId);
            throw new ServiceException("【" + userId + "】 登录已失效");
        }
        return videoStreamHelper.getVideoToken(areaCode);
    }

    /**
     * 根据层级Id获取慧眼token
     *
     * @param deviceId
     * @return
     */
    public String getTokenByDeviceId(String deviceId) {
        String areaCode = DeviceGbUtil.extractAreaCodeByDeviceId(deviceId);
        return getTokenByAreaCode(areaCode);
    }

    /**
     * 根据 areaCode 获取慧眼 token, 试用默认的有所有设备权限的账号
     *
     * @param areaCode
     * @return
     */
    public String getTokenByAreaCode(String areaCode) {
        return videoStreamHelper.getVideoToken(areaCode);
    }
}
