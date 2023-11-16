package com.eshore.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.eshore.common.exception.BizException;

import java.util.Collection;

public class Assert {

    public static void isNull(Object obj, String msg) {
        if (obj == null) {
            throw new BizException(msg);
        }
    }

    public static void isNotNull(Object obj, String msg) {
        if (obj != null) {
            throw new BizException(msg);
        }
    }

    public static void isEmpty(String str, String msg) {
        if (StrUtil.isEmpty(str)) {
            throw new BizException(msg);
        }
    }


    public static <T> void isCollEmpty(Collection<T> coll, String msg) {
        if (CollUtil.isEmpty(coll)) {
            throwMsg(msg);
        }
    }

    public static <T> void isCollNotEmpty(Collection<T> coll, String msg) {
        if (!CollUtil.isEmpty(coll)) {
            throwMsg(msg);
        }
    }


    public static void throwMsg(String msg) {
        throw new BizException(msg);
    }

    public static void throwMsg(String format,String msg) {
        throw new BizException(String.format(format,msg));
    }

    public static void isFalse(boolean flag, String msg) {
        if (!flag) throwMsg(msg);
    }

    public static void isTrue(boolean flag, String msg) {
        if (flag) {
            throwMsg(msg);
        }
    }

    public static void deleteFail(boolean flag) {
        if (!flag) throwMsg("删除失败");
    }
}
