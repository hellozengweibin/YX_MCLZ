package com.eshore.encrypt.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.eshore.common.config.AesConfig;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.AesUtil;
import com.eshore.common.utils.RsaUtils;
import com.eshore.common.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;


@Component
@Slf4j
public class DecryptFilter extends OncePerRequestFilter {

    @Autowired
    private AesConfig aesConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> treeMap = new TreeMap<String, String>();
        //AES方式解密
        // treeMap.put("decrypt", "001");
        try {
            String key = aesConfig.getSecretKey();

            Enumeration<String> parameterNames = request.getParameterNames();

            if (aesConfig.isEnable() && CollUtil.isNotEmpty(parameterNames)) {
                // 开启加解密时，参数键的名称只能传指定的名字，不允许包含别的名字
                while (parameterNames.hasMoreElements()) {
                    String name = parameterNames.nextElement();
                    if (!name.equals(aesConfig.getRequestParamKeyName())) {
                        throw new ServiceException(String.format("参数名[%s]不合法", name));
                    }
                }
            }

            String secretData = request.getParameter(aesConfig.getRequestParamKeyName());
            if (ObjectUtil.isEmpty(secretData) || !aesConfig.isEnable()) {
                // 无参数 || 未开启加密 直接放过
                filterChain.doFilter(request, response);
                return;
            }

            if (aesConfig.isShowLog()) log.debug(">>>>>>requestParam解密开始，key:{},data:{}", key, secretData);

            String encryptInfo = request.getHeader("encryptInfo");
            String aesKey = RsaUtils.decryptByPrivate(encryptInfo, RsaUtils.getPrivateKey(RsaUtils.PRIVATE_KEY));
            String data = AesUtil.decrypt(secretData, aesKey, aesConfig.getIv());
            if (aesConfig.isShowLog()) log.debug(">>>>>>requestParam解密完成，明文：{}", data);

            //防止解析时，带%报错或者+号丢失
            data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            data = data.replaceAll("\\+", "%2B");
            data = URLDecoder.decode(data, "UTF-8");
            if (data != null) {
                // logger.info("after AES decrypt:" + data);
                String[] arrSplit = data.split("&");
                for (String strSplit : arrSplit) {
                    String[] arrSplitEqual = null;
                    arrSplitEqual = strSplit.split("=");
                    //解析出键值
                    if (arrSplitEqual.length > 1) {
                        treeMap.put(arrSplitEqual[0], arrSplitEqual[1]);
                    }
                }
                // treeMap.put("decrypt", "000");
            }
            //解密后的参数可以放入request的parameter中，使得后续代码中可以通过request.getParameter("")获取解密后的参数
            request = new DecryptHttpRequestWrapper(request, treeMap);
        } catch (ServiceException e) {
            ServletUtils.renderString(response, JSON.toJSONString(ResponseGenerator.genFailResult(e.getMessage())));
            return;
        } catch (Exception e) {
            log.error(">>>>>>>>>>>请求参数解密异常：{}", e);
            ServletUtils.renderString(response, JSON.toJSONString(ResponseGenerator.genFailResult("请求参数异常")));
            return;
        }
        // 如果uri中不包含background，则继续
        filterChain.doFilter(request, response);
    }
}