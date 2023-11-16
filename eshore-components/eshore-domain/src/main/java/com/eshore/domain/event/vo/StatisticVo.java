package com.eshore.domain.event.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName StatisticVo
 * @Description
 * @Author jianlin.liu
 * @Date 2023/5/6
 * @Version 1.0callback
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticVo {

    /**
     * 提交方式 1：自动，2：手动
     */
    private Integer submitWay = 1;

    /**
     * 统计类型
     */
    private Integer type;

    /**
     * 是否清空数据
     */
    private boolean clearAll = false;

    /**
     * 可指定统计的月份
     */
    private List<String> statisticMonthList;

    /**
     * 是否只统计本月
     */
    private boolean onlyStatisticThisMonth = true;

    /**
     * 发起用户，默认system
     */
    private String userName = "system";

    private boolean wait = false;

}
