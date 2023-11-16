package com.eshore.statistic.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.utils.DateUtils;
import com.eshore.domain.model.vo.common.TimeRangeVo;
import com.eshore.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CommonStatisticHandler {

    @Resource
    protected ISysDeptService deptService;

    /**
     * 获取百分比
     *
     * @param num1
     * @param num2
     * @return
     */
    protected String getPercent(Long num1, Long num2) {
        if (ObjectUtil.isAllNotEmpty(num1, num2) && num2.intValue() != 0) {
            Double percent = NumberUtil.div(num1, num2, 2).multiply(BigDecimal.valueOf(100D)).doubleValue();
            return percent + "%";
        }
        return "0";
    }

    /**
     * @param thisMonth 是否本月
     * @return
     */
    protected TimeRangeVo getTimeRange(boolean thisMonth, List<String> monthList) {
        if (thisMonth) {
            Date monthFirstDayOf = DateUtils.getMonthFirstDayOf(0);
            Date monthLastDayOf = DateUtils.getMonthLastDayOf(0);
            return new TimeRangeVo(monthFirstDayOf, monthLastDayOf, DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        if (CollUtil.isNotEmpty(monthList)) {
            List<String> list = monthList.stream().sorted().collect(Collectors.toList());
            String minTime = DateUtil.format(DateUtils.getMonthFirstDayOf(list.get(0)),DateUtils.YYYY_MM_DD_HH_MM_SS) ;
            String maxTime = DateUtil.format(DateUtils.getMonthLastDayOf(list.get(list.size() - 1)),DateUtils.YYYY_MM_DD_HH_MM_SS) ;
            return new TimeRangeVo(minTime, maxTime);
        }
        return new TimeRangeVo(new Date(), new Date());
    }


    protected static <T extends TimeRangeVo> void setDefaultTimeRange(T timeRange, Integer beforeMonths, Integer beforeDays) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        if (ObjectUtil.isEmpty(timeRange.getStartTime())) {
            LocalDateTime startDateTime = yesterday;
            if (ObjectUtil.isNotEmpty(beforeMonths)) {
                startDateTime = startDateTime.minusMonths(beforeMonths);
            } else if (ObjectUtil.isNotEmpty(beforeDays)) {
                startDateTime = startDateTime.minusDays(beforeDays);
            }
            LocalDateTime startTime000 = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.MIN);
            String startTime = DateUtil.format(startTime000, DatePattern.NORM_DATETIME_PATTERN);
            timeRange.setStartTime(startTime);
        }
        // 结束时间 为 昨天最大时刻
        LocalDateTime endDateTime = yesterday;
        LocalDateTime endLocalTime = LocalDateTime.of(endDateTime.toLocalDate(), LocalTime.MAX);
        String endTime = DateUtil.format(endLocalTime, DatePattern.NORM_DATETIME_PATTERN);
        timeRange.setEndTime(endTime);
    }

}
