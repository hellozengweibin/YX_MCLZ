package com.eshore.business.utils;


import lombok.extern.slf4j.Slf4j;
import java.util.TimerTask;

/**
 * 异步方法工具类
 *
 * @author
 * @date 20221008
 */
@Slf4j
public class AsyncUtil {

    /**
     * 异步示例
     * AsyncManager.me().execute(AsyncUtil.saveCallLog(msg));
     * @param msg
     * @return
     */
    public static TimerTask saveCallLog(String msg) {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println(msg);
            }
        };
    }



}
