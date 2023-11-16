package com.eshore.statistic.controller;

import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.exception.ServiceException;
import com.eshore.statistic.enums.StatisticType;
import com.eshore.domain.event.StatisticEvent;
import com.eshore.domain.event.vo.StatisticVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;


/**
 * @ClassName StatisticController
 * @Description 统计控制器(用于手动出发统计)
 * @Author jianlin.liu
 * @Date 2023/7/11
 * @Version 1.0
 **/
@Slf4j
@Api(value = "/statistic/event/launch", tags = {"统计事件控制"})
@RestController
@RequestMapping(value = "/statistic/event/launch")
public class StatisticController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "type", value = ""),
            @ApiImplicitParam(paramType = "query", dataType = "boolean", name = "isClear", value = "")
    })
    @ApiOperation(value = "发起统计事件", notes = "发起统计事件", httpMethod = "POST")
    @PostMapping(value = "/{type}")
    public CommonResult<Boolean> launchStatisticEvent(@PathVariable("type") Integer type,
                                                      @RequestParam(value = "isClear", required = false, defaultValue = "false") Boolean isClear) {
        StatisticType statisticType = StatisticType.findByCode(type);
        if (statisticType == null) throw new ServiceException(String.format("暂不支持[type=%s]的统计类型", type));
        StatisticVo statisticVo = StatisticVo.builder()
                .type(type)
                .submitWay(2).build();
        StatisticEvent event = new StatisticEvent(statisticVo);
        applicationEventPublisher.publishEvent(event);
        return ResponseGenerator.genSuccessResult();
    }
}
