package com.eshore.statistic.consts;

public class StatisticConstants {

    /**
     * 表名分隔符
     */
    public static final String TABLE_SEPERATOR = "_";

    /**
     * 统计表基础前缀
     */
    public static final String STATISTIC_TABLE_NAME_PREFIX = "statistic";

    /**
     * 统计锁key，作为分布式锁的key
     */
    public static final String STATISTIC_LOCK_KEY = "STATISTIC:%s:%s";

    /**
     * 统计service名称后缀
     */
    public static final String statisticHandlerNameSuffix = "StatisticHandler";

    /**
     * 1小时
     */
    public static final long EXPIRE_TIME_1_HOUR = 3600;

    /**
     * 30分钟
     */
    public static final long EXPIRE_TIME_30_MINUTES = 1800;
}
