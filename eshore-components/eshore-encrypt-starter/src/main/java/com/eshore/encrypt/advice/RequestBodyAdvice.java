package com.eshore.encrypt.advice;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eshore.common.annotation.EncryptIgnore;
import com.eshore.common.config.AesConfig;
import com.eshore.common.utils.AesUtil;
import com.eshore.common.utils.RsaUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName RsaAdviceController
 * @Description 控制前、后端加解密
 * @Author jianlin.liu
 * @Date 2022/9/13
 * @Version 1.0
 **/
@Slf4j
@ControllerAdvice(basePackages = "com.eshore.web.controller")
public class RequestBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice {

    @Autowired
    private AesConfig aesConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (aesConfig.isEnable()) {
            return !methodParameter.hasMethodAnnotation(EncryptIgnore.class);
        }
        return false;
    }

    /**
     * 服务器读取请求的时候进行，解密处理
     */
    @SneakyThrows
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (inputMessage.getBody().available() <= 0) {
            return inputMessage;
        }
        byte[] requestBodyBytes = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(requestBodyBytes);
        byte[] requsetNewBytes = null;
        // 解码，且解密后的数据
        HttpHeaders headers = inputMessage.getHeaders();
        // 从请求头获取 Aeskey =》 后端rsa私钥进行解密
        List<String> keys = headers.get("encryptInfo");
        String aesKey = RsaUtils.decryptByPrivate(keys.get(0), RsaUtils.getPrivateKey(RsaUtils.PRIVATE_KEY));
        if (aesConfig.isShowLog()) log.debug(">>>>>>>>>>>>>>>>>>aesKey:{}", aesKey);
        requsetNewBytes = descryHandler(requestBodyBytes, aesKey);
        InputStream arrayInputStream = new ByteArrayInputStream(requsetNewBytes);
        // 将解码的后的内容封装回原数据结构
        return new HttpInputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return arrayInputStream;
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 对body为空进行处理，即可能是通过路径传值
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }

    public byte[] descryHandler(byte[] requestBytes, String key) {
        if (requestBytes.length <= 0) {
            return new byte[0];
        }
        String requestData = new String(requestBytes, StandardCharsets.UTF_8);
        //根据请求体的格式进行对应的修改。
        JSONObject jsonObject = JSON.parseObject(requestData);
        if (aesConfig.isEnable()) {
            if (jsonObject != null) {
                if ((!jsonObject.containsKey(aesConfig.getRequestBodyKeyName())) || jsonObject.size() > 1) {
                    throw new HttpMessageNotReadableException("请求参数异常");
                }
            }
        }
        // 请求体封装的类 【前后端对数据格式的联调】
        String encrypt = jsonObject.get(aesConfig.getRequestBodyKeyName()) == null ? "" : jsonObject.get(aesConfig.getRequestBodyKeyName()).toString();
        String decrypt = "";
        if (encrypt.length() > 0) {
            try {
                // 从请求头获取key
                decrypt = AesUtil.decrypt(encrypt, key, aesConfig.getIv());
            } catch (Exception e) {
                log.error("解码失败：{}", e);
            }
        }
        return decrypt.getBytes(StandardCharsets.UTF_8);
    }


}
