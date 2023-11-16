package com.eshore.encrypt.strategy;


import com.eshore.common.utils.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 字段脱敏策略工厂
 * Author: jianlin.liu
 * Date: 2023-07-26
 */
public class DesensitizationStrategyFactory {
    private static final Map<Integer, AbstractDesensitizationStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer type, AbstractDesensitizationStrategy strategy) {
        STRATEGY_MAP.put(type, strategy);
    }

    public static AbstractDesensitizationStrategy getStrategyNoNull(Integer type) {
        AbstractDesensitizationStrategy strategy = STRATEGY_MAP.get(type);
        Assert.isNull(strategy, "未适配到脱敏策略");
        return strategy;
    }
}
