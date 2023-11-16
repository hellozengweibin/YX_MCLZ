package com.eshore.business.constant;

/**
 * @author Admin
 */
public class CommonConstants {
    /**eshore_template:expired:{type}:{sn}
     * 过期
     */
    public static final String REDIS_KEY_EXPIRED = "eshore_template:expired:";

    /**
     * 过期
     */
    public static final String REDIS_KEY_SEPARATOR = ":";

    /**
     * ai_box:alarm:dialog:{channelId}:{algorithmCode}
     */
    public static final String REDIS_KEY_ALARM_SHOW_DIALOG = "eshore_template:alarm:dialog:";

    public static final String MESSAGE_MAP = "MESSAGE_MAP";  // 短信验证码缓存
    public static final Integer MESSAGE_MAP_TIME = 900;                  // 登录验证码  缓存15分钟


    public static final String MESSAGE_MAP_SEND_MAX_ONE_DAY = "SEND_MAX_ONE_DAY";  // 短信验证码每个账号每天最大的短信验证码数量
    public static final String MESSAGE_MAP_SEND_MAX_INTERVAL = "SEND_MAX_INTERVAL";  // 短信验证码每个账号每间隔内最大的短信验证码数量

    public static final String REQUEST_MAP = "REQUEST_MAP";  // 请求缓存
    public static final String DOWNLOAD_MAP = "DOWNLOAD_MAP";  // 下载缓存

}
