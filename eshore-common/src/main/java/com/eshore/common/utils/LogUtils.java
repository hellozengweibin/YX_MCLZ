package com.eshore.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理并记录日志文件
 *
 * @author eshore
 */
@Slf4j
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

    public static void log(String url, Object req, Object resp) {
        log.info("\n 请求url:{} \n 请求参数为:{} \n 返回结果为：{} \n", url, JSON.toJSONString(req), resp);
    }


}
