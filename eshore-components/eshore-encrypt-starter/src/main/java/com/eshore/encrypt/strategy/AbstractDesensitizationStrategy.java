package com.eshore.encrypt.strategy;

import com.eshore.domain.annotation.FieldDesensitize;

import javax.annotation.PostConstruct;

/**
 * @ClassName AbstractDesensitizationStrategy
 * @Description AbstractDesensitizationStrategy 抽象脱敏策略
 * @Author jianlin.liu
 * @Date 2023/7/26
 * @Version 1.0
 **/
public abstract class AbstractDesensitizationStrategy {


    /**
     * 脱敏类型
     * @return
     */
    protected abstract FieldDesensitize.Strategy getTypeEnum();

    @PostConstruct
    public void init() {
        DesensitizationStrategyFactory.register(getTypeEnum().getValue(), this);
    }

    /**
     * 脱敏处理逻辑
     * @return
     */
    public abstract String execStrategy(String val);

}
