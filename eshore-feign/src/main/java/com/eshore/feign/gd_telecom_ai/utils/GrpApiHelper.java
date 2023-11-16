package com.eshore.feign.gd_telecom_ai.utils;

import cn.hutool.crypto.digest.MD5;
import com.eshore.feign.gd_telecom_ai.request.GrpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shanglangxin
 * @since 2022/7/11 11:24
 */
public class GrpApiHelper {

    private static final String HEADER_TIMESTAMP = "Timestamp";
    private static final String HEADER_APP_REQUEST_ID = "appRequestId";
    private static final String HEADER_APP_KEY = "AppKey";
    private static final String HEADER_SIGN = "Sign";

    public static String getSign(String appKey, String appSecret, String requestId, long timestamp) {
        return MD5.create().digestHex(appKey + appSecret + timestamp + requestId);
    }

    public static Map<String, String> getCommonHeaderMap(GrpRequest request) {
        return new HashMap<String, String>(MapUtil.capacity(4)) {{
            put(HEADER_TIMESTAMP, String.valueOf(request.getTimestamp()));
            put(HEADER_APP_REQUEST_ID, request.getAppRequestId());
            put(HEADER_APP_KEY, request.getConfig().getAppKey());
            put(HEADER_SIGN, getSign(request.getConfig().getAppKey(), request.getConfig().getAppSecret()
                    , request.getAppRequestId(), request.getTimestamp()));
        }};
    }

}
