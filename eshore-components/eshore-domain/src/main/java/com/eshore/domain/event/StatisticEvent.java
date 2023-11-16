package com.eshore.domain.event;

import com.eshore.domain.event.vo.StatisticVo;
import com.eshore.domain.notify.INotify;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName StatisticEvent
 * @Description 统计事件
 * @Author jianlin.liu
 * @Date 2023/07/11
 * @Version 1.0
 **/
@Getter
public class StatisticEvent extends ApplicationEvent {

    private StatisticVo statisticVo;

    private INotify notify;

    public StatisticEvent(StatisticVo statisticVo) {
        super(statisticVo);
        this.statisticVo = statisticVo;
    }

    public StatisticEvent(StatisticVo statisticVo, INotify notify) {
        super(statisticVo);
        this.statisticVo = statisticVo;
        this.notify = notify;
    }

}
