package com.eshore.common.exception;

/**
 * @author chenpeng.huang
 * @since 2021/1/18
 *
 * 异常枚举接口
 */
public interface ServiceExceptionEnumInterface {

    /**
     * 获取异常信息码
     * @return
     */
    Integer getCode();

    /**
     * 获取异常信息
     * @return
     */
    String getMessage();
}
