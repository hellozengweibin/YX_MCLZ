package com.eshore.domain.validator;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.domain.annotation.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @ClassName EnumValueValidator
 * @Description 枚举校验注解处理类
 * @Author jianlin.liu
 * @Date 2022/8/19
 * @Version 1.0
 **/
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private String[] strValues;
    private int[] intValues;
    private boolean require;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
        this.require = constraintAnnotation.require();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (require) {
            if (Objects.isNull(value) || ObjectUtil.isEmpty(value)) {
                return false;
            }
            return validate(value);
        }
        //非必填
        if (ObjectUtil.isNotEmpty(value)) {
            return validate(value);
        }
        //为空不校验
        return true;
    }

    /**
     * 校验
     *
     * @param value
     * @return
     */
    private boolean validate(Object value) {
        if (value instanceof String) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer) {
            for (int s : intValues) {
                if (s == ((Integer) value).intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void validateEnumValue(EnumValue enumValue, Object value) {
        if (enumValue.require()) {
            if (Objects.isNull(value) || ObjectUtil.isEmpty(value)) {
                throw new RuntimeException(enumValue.message());
            }
            validate(enumValue, value);
            return;
        }
        //非必填
        if (ObjectUtil.isNotEmpty(value)) {
            validate(enumValue, value);
        }
    }

    public static boolean validate(EnumValue enumValue, Object value) {
        if (value instanceof String) {
            for (String s : enumValue.strValues()) {
                if (s.equals(value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer) {
            for (int s : enumValue.intValues()) {
                if (s == ((Integer) value).intValue()) {
                    return true;
                }
            }
        }
        return false;
    }
}
