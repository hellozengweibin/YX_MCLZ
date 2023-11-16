package com.eshore.statistic.handler;

import com.eshore.domain.event.vo.StatisticVo;
import com.eshore.domain.model.vo.common.CallBackResult;
import com.eshore.domain.model.vo.common.TimeRangeVo;
import org.springframework.stereotype.Service;

/**
 * @ClassName OperationDeptStatisticHandler
 * @Description 运维单位统计
 * @Author jianlin.liu
 * @Date 2023/7/11
 * @Version 1.0
 **/
@Service("operationDeptStatisticHandler")
public class OperationDeptStatisticHandler extends CommonStatisticHandler implements IStatisticHandler {


    @Override
    public boolean supportRealTime() {
        return false;
    }

    @Override
    public CallBackResult handle(StatisticVo statisticVo) {
        TimeRangeVo timeRange = super.getTimeRange(statisticVo.isOnlyStatisticThisMonth(), statisticVo.getStatisticMonthList());

        return new CallBackResult<>(true, null, null);
    }
}
