package com.eshore.framework.interceptor.impl;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.annotation.PlatformScope;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.framework.interceptor.PlatformInterceptor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName PlatformTypeInterceptorImpl
 * @Description 平台类型拦截器
 * @Author jianlin.liu
 * @Date 2023/7/14
 * @Version 1.0
 **/
@Component
public class PlatformTypeInterceptorImpl extends PlatformInterceptor {

    @Override
    public boolean isPass(HttpServletRequest request, PlatformScope annotation) {
        PlatformType[] types = annotation.types();
        if (ObjectUtil.isEmpty(types)) return true;
        if (types.length == 0) return true;

        PlatformType loginUserCurrentPlatform = SecurityUtils.getLoginUserCurrentPlatform();
        if (ObjectUtil.isEmpty(loginUserCurrentPlatform)) {
            return false;
        }

        for (PlatformType platformType : types) {
            if (platformType.getCode() == loginUserCurrentPlatform.getCode()) {
                return true;
            }
        }

        return false;
    }
}
