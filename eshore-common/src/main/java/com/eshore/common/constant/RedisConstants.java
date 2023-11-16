package com.eshore.common.constant;

public class RedisConstants {

    /**
     * 视频token前缀
     */
    public static final String VIDEO_TOKEN_KEY_PREFIX = "VIDEO_TOKEN:";
    /**
     * 获取视频token失败的用户，缓存在redis中，30分钟内不再请求
     */
    public static final String VIDEO_TOKEN_FAIL_KEY_PREFIX = "VIDEO_TOKEN_FAIL:";
    /**
     * 视频token的过期时间
     */
    public static final long SECOND_MINUTE = 120;

    public static final int SIX_MINUTE = 6 * 60;
    public static final int FIVE_MINUTE = 300;

    public static final int THIRTY_MINUTE = 60 * 30;

    /**
     * 用户登录失败前缀
     */
    public static final String LOGIN_ERROR_KEY_PREFIX = "LOGIN_ERROR_KEY_PREFIX:";
    public static final String LOGIN_ERROR_TIMES = "LOGIN_ERROR_TIMES:";


   public static final int LOGIN_ERROR_MAX_TIMES = 5;


   public static final int LOGIN_ERROR_FORBIDDEN_TIME = 60 * 60;
}
