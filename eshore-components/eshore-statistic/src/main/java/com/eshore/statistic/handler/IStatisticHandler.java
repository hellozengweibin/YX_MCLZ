package com.eshore.statistic.handler;

import com.eshore.domain.event.vo.StatisticVo;
import com.eshore.domain.model.vo.common.CallBackResult;

/**
 * @InterfaceName IStatisticHandler
 * @Description
 * @Author jianlin.liu
 * @Date 2023/5/6
 * @Version 1.0
 **/
public interface IStatisticHandler<T> {

    /**
     * 是否支持实时统计
     *
     * @return
     */
    boolean supportRealTime();

    /**
     * 数据统计处理
     *
     * @param statisticVo
     * @return
     */
    CallBackResult<T> handle(StatisticVo statisticVo);

}
