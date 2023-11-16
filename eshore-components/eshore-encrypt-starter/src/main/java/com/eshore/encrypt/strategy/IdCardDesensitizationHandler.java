package com.eshore.encrypt.strategy;

import com.eshore.common.utils.StringUtils;
import com.eshore.domain.annotation.FieldDesensitize;
import org.springframework.stereotype.Component;

/**
 * @ClassName IdCardDesensitizationHandler
 * @Description 身份证号脱敏处理器
 * @Author jianlin.liu
 * @Date 2023/7/26
 * @Version 1.0
 **/
@Component
public class IdCardDesensitizationHandler extends AbstractDesensitizationStrategy{
    @Override
    protected FieldDesensitize.Strategy getTypeEnum() {
        return FieldDesensitize.Strategy.ID_CARD;
    }

    @Override
    public String execStrategy(String val) {
        if(StringUtils.isEmpty(val)) return val;
        String str = val.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
        return str;
    }
}
