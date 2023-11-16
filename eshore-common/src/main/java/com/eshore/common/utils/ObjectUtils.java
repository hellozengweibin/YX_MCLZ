package com.eshore.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectUtils {

    /**
    * author:walker
    * time: 2022/7/19 0019 20:30
    * description:  根据字段名获取字段值
    */
    public static Object getFieldValueByName(Object o, String fieldName) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * 给对象指定的属性设置值
     * @param obj  要设置值的对象
     * @param fieldName   要设置值的属性
     * @param value 值
     */
    public static void setFieldValueByName(Object obj, String fieldName, Object value){
        try {
            // 获取obj类的字节文件对象
            Class c = obj.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(obj, value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
