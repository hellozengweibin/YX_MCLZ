package com.eshore.video.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 地市对应枚举
 *
 * @author yanshaojian
 * @date 2021-12-15
 */
public enum AreaCodeEnum {

    /**
     * 地市对应枚举
     */
    GZ("4401", "gz", "广州市"),
    SZ("4403", "sz", "深圳市"),
    ZH("4404", "zh", "珠海市"),
    ST("4405", "st", "汕头市"),
    FS("4406", "fs", "佛山市"),
    SG("4402", "sg", "韶关市"),
    ZJ("4408", "zj", "湛江市"),
    ZQ("4412", "zq", "肇庆市"),
    JM("4407", "jm", "江门市"),
    MM("4409", "mm", "茂名市"),
    HZ("4413", "hz", "惠州市"),
    MZ("4414", "mz", "梅州市"),
    SW("4415", "sw", "汕尾市"),
    HY("4416", "hy", "河源市"),
    YJ("4417", "yj", "阳江市"),
    QY("4418", "qy", "清远市"),
    DG("4419", "dg", "东莞市"),
    ZS("4420", "zs", "中山市"),
    CZ("4451", "cz", "潮州市"),
    JY("4452", "jy", "揭阳市"),
    YF("4453", "yf", "云浮市");

    private final String code;
    private final String alias;
    private final String name;

    AreaCodeEnum(String code, String alias, String name) {
        this.code = code;
        this.alias = alias;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private String getAlias() {
        return alias;
    }

    public static String getAliasByCode(String code) {
        for (AreaCodeEnum s : AreaCodeEnum.values()) {
            if (s.getCode().equals(code)) {
                return s.getAlias();
            }
        }
        return "gz";
    }

    public static String getAreaName(String code) {
        for (AreaCodeEnum s : AreaCodeEnum.values()) {
            if (s.getCode().equals(code)) {
                return s.getName();
            }
        }
        return "-";
    }

    /**
     * 字典
     *
     * @return
     */
    public static Map<String, String> getAreaCodeDict() {
        Map<String, String> map = Maps.newHashMapWithExpectedSize(values().length);
        for (AreaCodeEnum value : values()) {
            map.put(value.code, value.name);
        }
        return map;
    }
}
