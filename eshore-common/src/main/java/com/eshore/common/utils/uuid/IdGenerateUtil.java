package com.eshore.common.utils.uuid;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 唯一id生成工具类
 * @Author jianlin.liu
 * @Version v1.0.0
 * @Since 1.0
 * @Date 2022-06-01
 */
public class IdGenerateUtil {


    // 最近的时间戳
    private long lastTimestamp = 0;
    //机器id 2位
    private static String machineId = "01";
    // 0，并发控制
    private long sequence = 0L;
    // 序列号的最大值
    private final int sequenceMax = 9999;

    public IdGenerateUtil(String machineId) {
        this.machineId = machineId;
    }


    public String nextId(String prefix) {
        return (StringUtils.isNotEmpty(prefix) ? prefix : "") + nextId();
    }

    public String nextId(String prefix, String separator, int leftPadLen) {
        return (StringUtils.isNotEmpty(prefix) ? prefix : "")
                + (StringUtils.isNotEmpty(separator) ? separator : "")
                + nextId(leftPadLen);
    }


    public String nextId() {
        return nextId(2);
    }

    /**
     * 生成唯一id号
     *
     * @param leftPadLen
     * @return
     */
    public synchronized String nextId(int leftPadLen) {
        Date now = new Date();
        String time = DateFormatUtils.format(now, "yyyyMMddHHmmssSS");
        long timestamp = now.getTime();
        if (this.lastTimestamp == timestamp) {
            // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环);
            // 对新的timestamp，sequence从0开始
            this.sequence = this.sequence + 1 % this.sequenceMax;
            if (this.sequence == 0) {
                // 重新生成timestamp
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        this.lastTimestamp = timestamp;
        StringBuilder sb = new StringBuilder(time).append(machineId).append(leftPad(sequence, leftPadLen));
        return sb.toString();
    }

    /**
     * 补码
     *
     * @param i
     * @param n
     * @return
     */
    private String leftPad(long i, int n) {
        String s = String.valueOf(i);
        StringBuilder sb = new StringBuilder();
        int c = n - s.length();
        c = c < 0 ? 0 : c;
        for (int t = 0; t < c; t++) {
            sb.append("0");
        }
        return sb.append(s).toString();
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 获取实例（使用默认机器码：01）
     *
     * @return
     */
    public static IdGenerateUtil getInstance() {
        return new IdGenerateUtil(machineId);
    }

    /**
     * 通过传入机器码获取实例
     *
     * @param machineId
     * @return
     */
    public static IdGenerateUtil getInstance(String machineId) {
        return new IdGenerateUtil(machineId);
    }

    /**
     * 调用
     */
    public static void main(String[] args) {
        IdGenerateUtil orderId = IdGenerateUtil.getInstance();
        List<String> idList = new ArrayList<>();
        int length = 1000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < length; i++) {
            String nextId = orderId.nextId();
            idList.add(nextId);
            System.out.println("生成订单号：" + nextId);
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format(">>>>>>>>>生成%d个订单号，耗时：%d ms ", length, end - start));
        System.out.println("===============>是否有重复订单号：" + (idList.stream().distinct().collect(Collectors.toList()).size() == length));

    }
}
