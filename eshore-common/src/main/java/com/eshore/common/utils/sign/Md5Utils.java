package com.eshore.common.utils.sign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Md5加密方法
 *
 * @author eshore
 */
public class Md5Utils {
    private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes("UTF-8"));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        try {
            return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("not supported charset...{}", e);
            return s;
        }
    }


    /**
     * 32位大写加密
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得 MD5 摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转成32位大写
     */
    public static String stringToMD5(String plainText) {
        byte[] mdBytes = null;
        try {
            mdBytes = MessageDigest.getInstance("MD5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不存在！");
        }

        String mdCode = new BigInteger(1, mdBytes).toString(16);

        if (mdCode.length() < 32) {
            int a = 32 - mdCode.length();
            for (int i = 0; i < a; i++) {
                mdCode = "0" + mdCode;
            }
        }
        return mdCode.toUpperCase(); //返回32位大写
//        return mdCode;            // 默认返回32位小写
    }


    /**
     * 根据对象字段首字母进行排序，生成json
     */
    public static <T> String orderFieldByWord(T req) {
        Class<?> aClass = req.getClass();
        Field[] fields = aClass.getDeclaredFields();

        List<Field> fieldList = Arrays.asList(fields);
        fieldList.sort(Comparator.comparing(Field::getName));

        List<String> fieldNameList = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        System.out.println(fieldNameList);

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < fieldNameList.size(); i++) {
            String s = fieldNameList.get(i);
            Object value = getFieldValueByName(s, req);
            if (value == null) continue;
            sb.append("\"" + s + "\":" + value + ",");
        }

        sb.replace(sb.lastIndexOf(","), sb.length(), "");
        sb.append("}");
        System.out.println(sb.toString());
        return sb.toString();
    }


    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            System.out.println("获取属性值失败");
        }
        return null;
    }


}
