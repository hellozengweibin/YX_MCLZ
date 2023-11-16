package com.eshore.encrypt.strategy;

import com.eshore.domain.annotation.FieldDesensitize;
import org.springframework.stereotype.Component;

/**
 * @ClassName RealNameDesensitizationHandler
 * @Description 姓名号脱敏处理器
 * @Author jianlin.liu
 * @Date 2023/7/26
 * @Version 1.0
 **/
@Component
public class RealNameDesensitizationHandler extends AbstractDesensitizationStrategy{
    @Override
    protected FieldDesensitize.Strategy getTypeEnum() {
        return FieldDesensitize.Strategy.REAL_NAME;
    }

    @Override
    public String execStrategy(String val) {
        return nameDesensitization(val);
    }

    /**
     * 名字脱敏
     * 规则，张三丰，脱敏为：张*丰
     *
     * @param name
     * @return
     */
    private String nameDesensitization(String name) {
        // 已经脱敏了直接返回
        if (name == null || name.contains("*")) {
            return name;
        }
        if (name == null || name.isEmpty()) {
            return "";
        }
        String myName = null;
        char[] chars = name.toCharArray();
        if (chars.length == 1) {
            myName = name;
        }
        if (chars.length == 2) {
            myName = org.apache.commons.lang3.StringUtils.overlay(name, "*", 1, 2);
            // myName = name.replaceFirst(name.substring(1), "*"); //todo
        }
        if (chars.length > 2) {
            int n = chars.length - 2;
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < n; i++) {
                s.append("*");
            }
            myName = org.apache.commons.lang3.StringUtils.overlay(name, String.valueOf(s), 1, chars.length - 1);
           // myName = name.replaceAll(name.substring(1, chars.length - 1), "*"); //todo
        }
        return myName;
    }
}
