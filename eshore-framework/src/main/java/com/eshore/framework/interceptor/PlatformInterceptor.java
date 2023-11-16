package com.eshore.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.eshore.common.annotation.PlatformScope;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.utils.ServletUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author eshore
 */
@Component
public abstract class PlatformInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PlatformScope annotation = method.getAnnotation(PlatformScope.class);
            if (annotation != null) {
                if (!this.isPass(request, annotation)) {
                    CommonResult ajaxCommonResult = ResponseGenerator.genFailResult(annotation.message());
                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxCommonResult));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isPass(HttpServletRequest request, PlatformScope annotation);
}
