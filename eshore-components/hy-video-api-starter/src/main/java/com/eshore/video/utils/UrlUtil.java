package com.eshore.video.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路径工具类 ymh 2023-03-24
 */
public class UrlUtil {

    /**
     * 获取根路径
     * @param url 源路径地址，如：http://30.168.60.333:2222/eduattendance
     * @return 根路径，格式：http://ip:port/
     */
    public static String getUrlRoot(String url){
        Pattern p = Pattern.compile("([a-zA-Z]+)\\:(\\//\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+\\/)");
        Matcher m = p.matcher(url);
        if(m.find()){
            System.out.println("url:" + m.group(0));
            return m.group(0);
        }
        return null;
    }
}
