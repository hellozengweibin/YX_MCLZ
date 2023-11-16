package com.eshore.encrypt.advice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.annotation.EncryptIgnore;
import com.eshore.common.config.AesConfig;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.AesUtil;
import com.eshore.common.utils.RsaUtils;
import com.eshore.common.utils.reflect.ReflectUtils;
import com.eshore.domain.annotation.FieldDesensitize;
import com.eshore.encrypt.model.EncryptResult;
import com.eshore.encrypt.strategy.AbstractDesensitizationStrategy;
import com.eshore.encrypt.strategy.DesensitizationStrategyFactory;
import com.eshore.encrypt.util.JsonUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.crypto.Cipher;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author jianlin.liu
 * @Date 2022/10/18 16:21
 * @Version 1.0
 */
@ControllerAdvice(basePackages = "com.eshore.web.controller")
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice {

    @Autowired
    private AesConfig aesConfig;

    private static final String UUID = "uuid";

    private final static List<String> EXPOSE_HEADERS = Collections.singletonList(UUID);

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if (aesConfig.isEnable()) {
            return !returnType.hasMethodAnnotation(EncryptIgnore.class);
        }
        return false;
    }

    // 为对response,也就是可以对响应的内容进行处理
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (ObjectUtil.isNotEmpty(body)) {
            // 返回数据脱敏
            this.handleFieldDesensitization(body);

            // String timeStamp = System.currentTimeMillis() +"000";
            // 对数据进行加密，加密响应体body中的data数据
            Cipher instance = Cipher.getInstance("AES/CBC/pkcs5padding");
            EncryptResult encryptResult = new EncryptResult();
            // 🐳这里加密后偏移量会有空格导致前端无法正常解析。且Aes加密秘钥并最好不要超过16位
            List<String> keysByRsa = request.getHeaders().get("encryptInfo");
            if (CollUtil.isEmpty(keysByRsa)) {
                throw new ServiceException("系统加解密发生错误，请联系管理员");
            }
            String key = keysByRsa.get(0);
            String aesKey = RsaUtils.decryptByPrivate(key, RsaUtils.getPrivateKey(RsaUtils.PRIVATE_KEY));
            String encrypt = AesUtil.encrypt(instance, JsonUtil.toJson(body), aesKey, aesConfig.getIv());
            encryptResult.setResponseData(encrypt);
            // 设置暴露的响应头
            List<String> checkIds = request.getHeaders().get("uuid");
            response.getHeaders().setAccessControlExposeHeaders(EXPOSE_HEADERS);
            response.getHeaders().add(UUID, checkIds.get(0));
            return encryptResult;
        }
        return body;
    }

    /**
     * 处理字段脱敏
     * @param obj
     */
    private void handleFieldDesensitization(Object obj){
        if(obj instanceof Map || obj == null) return;
        // 获取对象的全部属性字段
        List<Field> fieldList = ReflectUtils.getModelFields(obj);
        if(CollUtil.isEmpty(fieldList)) return;

        for (Field field : fieldList) {
            String fieldName = field.getName();
            Object fieldValue = ReflectUtils.getFieldValue(obj, fieldName);

            if (fieldValue instanceof Collection) {
                Collection list = (Collection) fieldValue;
                for (Object o : list) {
                    handleFieldDesensitization(o);
                }
                continue;
            }

            // 字段为空时不处理
            if(ObjectUtil.isEmpty(fieldValue)) continue;

            // 字段类型不为String时不处理
            if(!(fieldValue instanceof String)) continue;


            FieldDesensitize annotation = field.getAnnotation(FieldDesensitize.class);
            // 字段没有脱敏注解标记时，直接跳过
            if(!field.isAnnotationPresent(FieldDesensitize.class) && annotation == null) continue;

            // 执行数据脱敏处理
            int type = annotation.strategy().getValue();
            AbstractDesensitizationStrategy desensitizationStrategy = DesensitizationStrategyFactory.getStrategyNoNull(type);
            String desensitizedVal = desensitizationStrategy.execStrategy((String) fieldValue);
            if(desensitizedVal == null) continue;

            // 把脱敏后的数据重新set回对象
            ReflectUtils.invokeSetter(obj,fieldName,desensitizedVal);
        }
    }
}
