package com.eshore.domain.notify;


import com.eshore.domain.model.vo.common.CallBackResult;

/**
 * @InterfaceName CallBack
 * @Description 回调
 * @Author jianlin.liu
 * @Date 2023/5/12
 * @Version 1.0
 **/
@FunctionalInterface
public interface INotify<T extends CallBackResult> {

    /**
     * @param obj
     */
    void call(T obj);
}
