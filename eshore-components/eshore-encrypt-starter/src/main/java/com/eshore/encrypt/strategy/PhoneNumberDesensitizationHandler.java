package com.eshore.encrypt.strategy;

import com.eshore.common.utils.StringUtils;
import com.eshore.domain.annotation.FieldDesensitize;
import org.springframework.stereotype.Component;

/**
 * @ClassName PhoneNumberDesensitizationHandler
 * @Description 手机号脱敏处理器
 * @Author jianlin.liu
 * @Date 2023/7/26
 * @Version 1.0
 **/
@Component
public class PhoneNumberDesensitizationHandler extends AbstractDesensitizationStrategy{
    @Override
    protected FieldDesensitize.Strategy getTypeEnum() {
        return FieldDesensitize.Strategy.PHONE_NUMBER;
    }

    @Override
    public String execStrategy(String val) {
        if(StringUtils.isEmpty(val)) return val;
        return val.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
