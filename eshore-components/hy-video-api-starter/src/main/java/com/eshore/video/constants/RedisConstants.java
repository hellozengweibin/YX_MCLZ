package com.eshore.video.constants;

public class RedisConstants {

    /**
     * 视频token前缀 : VIDEO_TOKEN:{areaCode}:{account}
     */
    public static final String VIDEO_TOKEN_KEY = "VIDEO_TOKEN:%s:%s";
    /**
     * 视频账号key：VIDEO_ACCOUNT:{areaCode}
     */
    public static final String VIDEO_ACCOUNT_KEY = "VIDEO_ACCOUNT:%s";
    /**
     * 视频连接地址key：VIDEO_CONNECT_ADDRESS:{areaCode}
     */
    public static final String VIDEO_CONNECT_ADDRESS_KEY = "VIDEO_CONNECT_ADDRESS:%s";

    /**
     * 用户登录失败前缀
     */
    public static final String LOGIN_ERROR_KEY_PREFIX = "LOGIN_ERROR_KEY_PREFIX:";
    public static final String LOGIN_ERROR_TIMES = "LOGIN_ERROR_TIMES:";

    public static final String DEVICE_ALGORITHM = "DEVICE:ALGORITHM:%s:%s";

    /**
     * 算法告警回调处理Redis Key：DEVICE:ALGORITHM:INTERVAL:{deviceId}:{algorithmCode}
     */
    public static final String DEVICE_ALGORITHM_INTERVAL_KEY = "DEVICE:ALGORITHM:INTERVAL:%s:%s";

    public static final String JOB_ALGORITHM = "JOB:ALGORITHM:%s:%s";

    public static final String DUPLICATE_SNAPSHOT_KEY = "CALLBACK:SNAPSHOT:TYPE:%s:ID:%s";

    /**
     * 存储的1400绑定平台的设备LOCATION key：DEVICE_LOCATION:{platformId}
     */
    public static final String DEVICE_LOCATION = "DEVICE_LOCATION:%s";

    /**
     * 存储的1400绑定平台LOCATION key：PLATFORM_LOCATION:{platformId}
     */
    public static final String PLATFORM_LOCATION = "PLATFORM_LOCATION:%s";


    public static final String DUPLICATE_PERSON_SNAPSHOT_KEY = "CALLBACK:SNAPSHOT:DID:%s:ATTR:%s";

    public static final String ALARM_DATA_PREPARE_LOCK ="alarm:data:lock";


    public interface Lock{
        //告警统计
        String ALARM_STASTICS="lock:alarm:stastcis:";
        //告警百分比
        String ALARM_PERCENT="lock:alarm:percent:";
    }

    public interface DeviceStastics{
        //每日告警
        String DAILY_ALARM="stastics:dept:daily:alarm:";
        //预警数
        String SUM_ALARM="stastics:dept:sum:alarm:";
        //未查看预警数
        String NOTCHECK_ALARM="stastics:dept:notCheck:alarm:";

        //人脸
        String DAILY_FACE="stastics:device:daily:face";
        String SUM_FACE="stastics:device:sum:face";
        //车辆
        String DAILY_VEHICLE="stastics:device:daily:vehicle";
        String SUM_VEHICLE="stastics:device:sum:vehicle";
    }

    /**
    * 用户部门统计
    */
    public interface UserDeptStastics{
        //每日告警
        String DAILY_ALARM="stastics:user:dept:daily:alarm";
        //预警数
        String SUM_ALARM="stastics:user:dept:sum:alarm";
        //未查看预警数
        String NOTCHECK_ALARM="stastics:user:dept:notCheck:alarm";

        //算法告警
        String ALGORITHM_ALARM="stastics:user:dept:algorithmId:alarm";


    }

    public interface ClientHomeStatistic {

        String PREFIX = "CLIENT:STATISTIC:";
        /**
         * 预警区域分析
         */
        String ALARM_REGION_ANALYSIS = PREFIX + "ALARM_REGION:";

        /**
         * 算法趋势
         */
        String ALGORITHM_TREND = PREFIX + "ALGORITHM_TREND:";

        /**
         * 抓拍趋势
         */
        String CAPTURED_TREND = PREFIX + "CAPTURED_TREND:";

        /**
         * 抓拍区域
         */
        String CAPTURED_REGION = PREFIX + "CAPTURED_REGION:";
    }

}
