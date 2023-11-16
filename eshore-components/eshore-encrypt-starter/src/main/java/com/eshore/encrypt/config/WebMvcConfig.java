// package com.eshore.encrypt.config;
//
// import com.eshore.encrypt.intercepter.ParamInterceptor;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// import javax.annotation.Resource;
//
// /**
//  * @ClassName WebMvcConfig
//  * @Description
//  * @Author jianlin.liu
//  * @Date 2023/7/19
//  * @Version 1.0
//  **/
// @Configuration
// public class WebMvcConfig implements WebMvcConfigurer {
//
//     @Resource
//     private ParamInterceptor paramInterceptor;
//
//     /**
//      * 自定义拦截规则
//      */
//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         registry.addInterceptor(paramInterceptor).addPathPatterns("/**");
//     }
// }
