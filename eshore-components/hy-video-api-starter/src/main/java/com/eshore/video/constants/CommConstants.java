package com.eshore.video.constants;

import java.util.HashMap;
import java.util.Map;

public interface CommConstants {

    String PROJECT_NAME = "aiPlatform";

    public static final class MSG_VER {
        public static Map<String, String> formulaMap = new HashMap<String, String>();

        static {
            formulaMap.put("1", "请求查询结果为空");
            formulaMap.put("2", "请求超时");
            formulaMap.put("3", "token失效");
            formulaMap.put("4", "请求失败(服务器内部错误)");
            formulaMap.put("5", "请求错误(客户端请求错误)");
            formulaMap.put("6", "有重复记录");
            formulaMap.put("7", "请求频繁");
            formulaMap.put("8", "验证码错误");
            formulaMap.put("9", "用户不存在");
            formulaMap.put("11", "用户名或密码错误，请重新输入");
            formulaMap.put("13", "无权限设备");
        }
    }

    public interface API {
        String RESULT_CODE_SUC = "0";
        int RESULT_CODE_SUCCESS = 0;
        String RESULT_CODE_EMPTY = "1";
        String RESULT_CODE_TIMEOUT = "3";
        String RESULT_CODE_DUPLICATE = "6";
    }

    public interface CIPHER_PADDING {
        String NO_PADDING = "AES/CBC/NoPadding";
        String PKCS5_PADDING = "AES/CBC/pkcs5padding";
    }

    public static final class ALARM_TYPE {
        public static Map<Integer, Integer> formulaMap = new HashMap<>();

        static {
            formulaMap.put(1, 901); // AI智搜-人脸识别事件
            formulaMap.put(2, 902); // AI智搜-车辆识别事件
            formulaMap.put(3, 903); // AI智搜-打架斗殴事件
            formulaMap.put(4, 904); // AI智搜-持刀持械事件
            formulaMap.put(5, 907); // 人员聚集
            formulaMap.put(5, 1000022); // 人员跌倒
            formulaMap.put(33, 906); // 一键警示

        }
    }

    /**
     * 短信验证码
     */
    public interface MESSAGE {
        String MESSAGE_MAP = "MESSAGE_MAP"; // 短信验证码缓存
        Integer MESSAGE_MAP_TIME = 300; // 短信验证码缓存时间为5分钟

        String MESSAGE_VALID_FAIL_TIME_MAP = "MESSAGE_VALID_FAIL_TIME_MAP"; // 登录验证码  5分钟内失败次数限制缓存
        Integer MESSAGE_VALID_FAIL_COUNT = 3; // 登录验证码  5分钟内失败次数限制
        Integer MESSAGE_VALID_FAIL_LOCK_TIME = 900; // 登录验证码  5分钟内失败三次锁定15分钟

        String MESSAGE_MAP_SEND_MAX_ONE_DAY = "SEND_MAX_ONE_DAY"; // 短信验证码每个账号每天最大的短信验证码数量
        String MESSAGE_MAP_SEND_MAX_INTERVAL = "SEND_MAX_INTERVAL"; // 短信验证码每个账号每间隔内最大的短信验证码数量

    }

    Long ROOT_DEPT_ID = 100L;

    Long ONE_CLICK_WARNING_ID = 33L;
    String ONE_CLICK_WARNING_NAME = "一键告警";

    String TRIGGER_TYPE_MANUAL = "1";
    String TRIGGER_TYPE_AI = "2";

    //违规停车
    String ILLEGAL_PARKING_ALGORITHM_CODE = "1000051";

    //未戴头盔
    String WITHOUT_HELMET_ALGORITHM_CODE = "1000040";
    Long WITHOUT_HELMET_ALGORITHM_ID = 9L;

    // 长者关爱告警
    String ELDERLY_CARE_ALGORITHM_CODE = "1000057";
}
