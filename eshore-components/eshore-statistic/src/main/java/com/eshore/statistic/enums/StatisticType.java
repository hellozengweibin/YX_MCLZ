package com.eshore.statistic.enums;

import lombok.Getter;

import static com.eshore.statistic.consts.StatisticConstants.*;

/**
 * @EnumName StatisticType
 * @Description
 * @Author jianlin.liu
 * @Date 2023/05/06
 * @Version 2.0
 **/
@Getter
public enum StatisticType {


    OPERATION_DEPT(1, "operationDept", "opt_dept", "运维单位统计"),
    ;


    private int code;

    private String handlerName;

    /**
     * 统计表后缀，默认以statistic_作为前缀
     */
    private String tableSuffix;

    private String desc;


    StatisticType(int code, String handlerName, String tableSuffix, String desc) {
        this.code = code;
        this.handlerName = handlerName;
        this.tableSuffix = tableSuffix;
        this.desc = desc;
    }

    public static StatisticType findByCode(Integer code) {
        if (code == null) return null;
        for (StatisticType value : values()) {
            if (value.getCode() == code.intValue()) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取统计处理器
     * @return
     */
    public String getStatisticHandler(){
        return this.handlerName + statisticHandlerNameSuffix;
    }

    /**
     * 获取统计表
     * @return
     */
    public String getStatisticTable(){
        return STATISTIC_TABLE_NAME_PREFIX + TABLE_SEPERATOR + this.tableSuffix;
    }

}
