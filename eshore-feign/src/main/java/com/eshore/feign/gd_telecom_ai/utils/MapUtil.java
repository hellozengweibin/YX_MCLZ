package com.eshore.feign.gd_telecom_ai.utils;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanglangxin
 * @since 2022/7/11 13:03
 */
public class MapUtil {

    public static int capacity(int expectedSize) {
        if (expectedSize < 0) {
            return 0;
        }
        return expectedSize < 1073741824 ? (int) ((float) expectedSize / 0.75F + 1.0F) : 2147483647;
    }

    /**
     * 对象转 map, 这里引用 alibaba 的 JsonField 注解
     * 只对当前类含有的字段
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(fields.length);
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Alias jsonField = field.getAnnotation(Alias.class);
                String key = Objects.nonNull(jsonField) && StrUtil.isNotBlank(jsonField.value()) ? jsonField.value() : field.getName();
                Object value = field.get(obj);
                map.put(key, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
}
