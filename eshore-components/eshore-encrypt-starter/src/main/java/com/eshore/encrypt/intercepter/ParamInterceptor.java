// package com.eshore.encrypt.intercepter;
//
// import cn.hutool.core.collection.CollUtil;
// import com.eshore.common.annotation.EncryptIgnore;
// import com.eshore.common.config.AESProperties;
// import com.eshore.common.exception.ServiceException;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.web.method.HandlerMethod;
// import org.springframework.web.servlet.HandlerInterceptor;
//
// import javax.annotation.Resource;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.lang.reflect.Method;
// import java.util.Enumeration;
//
// /**
//  * @ClassName ParamInterceptor
//  * @Description 参数连接器，当开启加解密时，只能url参数只能为指定的键名称
//  * @Author jianlin.liu
//  * @Date 2023/7/19
//  * @Version 1.0
//  **/
// @Slf4j
// @Component
// public class ParamInterceptor implements HandlerInterceptor {
//
//     @Resource
//     private AESProperties aesProperties;
//
//     @Override
//     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//         if (handler instanceof HandlerMethod) {
//             HandlerMethod handlerMethod = (HandlerMethod) handler;
//             Method method = handlerMethod.getMethod();
//             EncryptIgnore annotation = method.getAnnotation(EncryptIgnore.class);
//             if (annotation != null) {
//                 return true;
//             }
//
//             Enumeration<String> parameterNames = request.getParameterNames();
//
//             if (aesProperties.isEnable() && CollUtil.isNotEmpty(parameterNames)) {
//                 // 开启加解密时，参数键的名称只能传指定的名字，不允许包含别的名字
//                 while (parameterNames.hasMoreElements()) {
//                     String name = parameterNames.nextElement();
//                     if (!name.equals(aesProperties.getRequestParamKeyName())) {
//                         throw new ServiceException(String.format("参数名[%s]不合法", name));
//                     }
//                 }
//             }
//         }
//         return true;
//     }
//
// }
