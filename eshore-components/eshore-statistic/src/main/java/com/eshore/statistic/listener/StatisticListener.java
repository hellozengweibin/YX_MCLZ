package com.eshore.statistic.listener;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.utils.uuid.IdUtils;
import com.eshore.domain.event.StatisticEvent;
import com.eshore.domain.event.vo.StatisticVo;
import com.eshore.domain.model.vo.common.CallBackResult;
import com.eshore.statistic.enums.StatisticType;
import com.eshore.statistic.handler.IStatisticHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.eshore.statistic.consts.StatisticConstants.STATISTIC_LOCK_KEY;

/**
 * @ClassName StatisticListener
 * @Description 统计监听器
 * @Author jianlin.liu
 * @Date 2023/5/6
 * @Version 2.0
 **/
@Slf4j
@Component
public class StatisticListener implements ApplicationListener<StatisticEvent> {

    @Autowired
    private Map<String, IStatisticHandler> statisticHandlerMap;

    @Resource
    private ThreadPoolTaskExecutor asyncThreadPool;


    // @Async("asyncThreadPool")
    @SneakyThrows
    @Override
    public void onApplicationEvent(StatisticEvent event) {
        long start = System.currentTimeMillis();
        String uuid = IdUtils.fastSimpleUUID();
        log.info("[StatisticListener]=====>>>>>>>>[{}] => 接收到统计事件[{}]", uuid, event);
        StatisticVo statisticVo = event.getStatisticVo();
        if (statisticVo == null) {
            log.warn("[StatisticListener]>>>>>>>>>>>>>>>>statistic event is null");
            return;
        }
        Integer type = statisticVo.getType();
        StatisticType statisticType = StatisticType.findByCode(type);
        if (statisticType == null) {
            log.warn("[StatisticListener]>>>>>>>>>>>>>>>>statistic type is null");
            return;
        }
        IStatisticHandler handler = statisticHandlerMap.get(statisticType.getStatisticHandler());

        if (handler == null) {
            log.error("[StatisticListener]====>>>>>>>>>未找到type={}的统计类型处理器");
            return;
        }
        String lockKey = String.format(STATISTIC_LOCK_KEY, type, statisticType.getHandlerName());

        log.warn("[StatisticListener][{}]=========>lockKey:[{}],statisticType:{},handler:{},param:{} => 正在处理...",
                    uuid, lockKey, type, handler, statisticVo);

        // 异步执行
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info("[StatisticListener]=====>[{}]统计任务开始执行，加锁[{}]成功", statisticType.getDesc(), lockKey);
            String statisticTable = statisticType.getStatisticTable();
            if (statisticVo.isClearAll()) {
                log.info("[StatisticListener]============>清空统计表:[{}]", statisticTable);
            }
            CallBackResult result = handler.handle(statisticVo);
            if (ObjectUtil.isNotEmpty(result) && ObjectUtil.isNotEmpty(event.getNotify())) {
                event.getNotify().call(result);
            }
        }, asyncThreadPool).whenComplete((r, e) -> {

            log.info("[StatisticListener]===>[{}]统计任务，锁：{} 释放成功", statisticType.getDesc(), lockKey);
        }).exceptionally((e) -> {
            log.error("[StatisticListener]=====>[{}]统计任务异常 =>{}", statisticType.getDesc(), e);
            return null;
        });

        if (statisticVo.isWait()) {
            // 是否等待
            future.join();
        }
        log.info("[StatisticListener]=====>uuid:[{}] => [{}]统计类型处理完成，耗时：{}s",
                uuid, statisticType.getDesc(), (System.currentTimeMillis() - start) / 1000.0);
    }

}
