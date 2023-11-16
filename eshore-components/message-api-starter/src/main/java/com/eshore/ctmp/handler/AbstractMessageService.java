package com.eshore.ctmp.handler;

/**
 * @ClassName AbstractMessageService
 * @Description 抽象短信服务
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
public abstract class AbstractMessageService<T> implements MessageApi {

    protected abstract T buildConfig(T t);
}
