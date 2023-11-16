package com.eshore.feign.zgdx_shiJueZhiLian.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.eshore.common.utils.sign.Md5Utils;
import com.eshore.feign.zgdx_shiJueZhiLian.ShijueZhiLianService;
import com.eshore.feign.zgdx_shiJueZhiLian.req.AlarmQueryReq;
import com.eshore.feign.zgdx_shiJueZhiLian.req.BaseReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class ShijueZhiLianServiceImpl implements ShijueZhiLianService {

    @Value("${zgdx_shiJueZhiLian.commonUrl}")
    private String commonUrl;
    @Value("${zgdx_shiJueZhiLian.memberKey}")
    private String memberKey;
    @Value("${zgdx_shiJueZhiLian.secret}")
    private String secret;
    @Value("${zgdx_shiJueZhiLian.appKey}")
    private String appKey;


    interface ApiConstant {
        String ALARM_QUERY = "/log/alarm/query?appkey=%s";
    }


    @Override
    public void alarmQuery(AlarmQueryReq req) {
        String sign = getSign(req);
        String url = getUrl(String.format(ApiConstant.ALARM_QUERY, appKey));
        BaseReq baseReq = new BaseReq();
        baseReq.setSign(sign);
        baseReq.setParmdata(req);
        String res = HttpUtil.post(url, JSON.toJSONString(baseReq));
        log.info("请求url {}\n,请求参数 {} \n 返回结果：{}", url, JSON.toJSONString(baseReq), res);
    }


    private String getUrl(String url) {
        return commonUrl + url;
    }

    private String getSign(Object req) {
        String json = JSON.toJSONString(req);
        String raw = secret + "&&" + json.replaceAll(" ", "");
        String encode = Md5Utils.stringToMD5(raw);
        return encode;
    }

    private <T> void fieldByWord(T req) {
        Class<?> aClass = req.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();

        }
        List<Field> fieldList = Arrays.asList(fields);

        fieldList.sort(Comparator.comparing(Field::getName));

    }
}
