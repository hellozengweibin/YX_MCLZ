package com.eshore.video.utils;

import cn.hutool.core.util.StrUtil;
import com.eshore.video.constants.AreaCodeEnum;

/**
 * @author shanglangxin
 * @since 2022/10/27 14:11
 */
public class DeviceGbUtil {

    /**
     * 根据设备前4为判断设备所在的地市编号
     *
     * @param deviceId
     * @return
     */
    public static String extractAreaCodeByDeviceId(String deviceId) {
        return StrUtil.sub(deviceId, 0, 4);
    }

    /**
     * 根据设备前4 转换为地市名
     * @param deviceId
     * @return
     */
    public static String transformAreaName(String deviceId){
        return AreaCodeEnum.getAreaName(extractAreaCodeByDeviceId(deviceId));
    }

}
