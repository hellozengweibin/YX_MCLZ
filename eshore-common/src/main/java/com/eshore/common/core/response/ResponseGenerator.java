package com.eshore.common.core.response;

import com.github.pagehelper.PageInfo;

/**
 * @author jianlin.liu
 * @since 2022/07/01
 * <p>
 * 创建统一响应返回
 */
public class ResponseGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static <T> CommonResult<T> genSuccessResult() {
        return new CommonResult<T>()
                .setCode(ResponseCodeEnum.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> CommonResult<T> genSuccessResult(T data) {
        return new CommonResult<T>()
                .setCode(ResponseCodeEnum.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static <T> CommonResult<T> genFailResult(String message) {
        return new CommonResult<T>()
                .setCode(ResponseCodeEnum.FAIL)
                .setMessage(message);
    }


    public static <T> CommonResult<T> genFailResult(int code, String message) {
        return new CommonResult<T>()
                .setCode(code)
                .setMessage(message);
    }

    public static <T> CommonResult<T> genFailResult(int code, String message, T data) {
        return new CommonResult<T>()
                .setCode(code)
                .setMessage(message)
                .setData(data);
    }


    public static <T> CommonResult<PageResponseBody<T>> genSuccessPageResult(PageInfo<T> pageInfo) {
        return genSuccessResult(new PageResponseBody().setPageInfo(pageInfo));
    }

    /**
     * @param pageInfo
     * @param attach   附加数据
     * @param <T>
     * @return
     */
    public static <T> CommonResult<PageAttachResponseBody<T>> genSuccessPageAttachResult(PageInfo<T> pageInfo, Object attach) {
        return genSuccessResult(new PageAttachResponseBody().setPageInfo(pageInfo, attach));
    }
}
