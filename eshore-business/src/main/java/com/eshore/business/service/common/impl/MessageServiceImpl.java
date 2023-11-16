package com.eshore.business.service.common.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.eshore.business.constant.CommonConstants;
import com.eshore.business.service.common.IBizMessageSendRecordService;
import com.eshore.business.service.common.IMessageService;
import com.eshore.common.constant.CommConstants;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.DateUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.domain.entity.common.BizMessageSendRecord;
import com.eshore.domain.model.vo.common.MessageSendVo;
import com.eshore.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author yhq
 */
@Service
@SuppressWarnings("rawtypes")
public class MessageServiceImpl implements IMessageService {

    private static Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IBizMessageSendRecordService bizMessageSendRecordService;


    @Value("${messageConfig.url}")
    private String msgUrl;
    @Value("${messageConfig.account}")
    private String msgAccount;
    @Value("${messageConfig.password}")
    private String msgPassword;
    @Value("${messageConfig.msgPrefix}")
    private String msgPrefix;
    @Value("${messageConfig.maxOneDay}")
    private Integer maxOneDay;
    @Value("${messageConfig.maxInterval}")
    private Integer maxInterval;
    @Value("${messageConfig.intervalTime}")
    private Integer intervalTime;

    @Override
    public boolean validMessageCode(MessageSendVo messageSendVo, boolean isValidatedDelete) {
        String phoneNum = messageSendVo.getPhoneNum();
        String appCode = messageSendVo.getAppCode();
        int type = messageSendVo.getType();
        String messageCode = messageSendVo.getMessageCode();
        String account = messageSendVo.getAccount();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // if (StringUtils.isBlank(phoneNum)) {
        //     throw new ServiceException("手机号码不能为空");
        // }

        if (StringUtils.isEmpty(messageCode)) {
            throw new ServiceException("验证码不能为空");
        }
        if (StringUtils.isEmpty(account)) {
            throw new ServiceException("登录账号不能为空");
        }
        if (StringUtils.isEmpty(phoneNum)) {
            SysUser sysUser = sysUserService.selectUserByUserName(account);
            if (sysUser == null) {
                throw new ServiceException("验证码不存在或已使用");
            }
            phoneNum = sysUser.getPhonenumber();
        }
        String key = CommonConstants.MESSAGE_MAP + ":" + ":" + type + ":" + phoneNum;
        Boolean bl = redisCache.hasCacheKey(key);

        // 连续输错验证码次数校验
        Integer messageValidFailLockTime = CommConstants.MESSAGE.MESSAGE_VALID_FAIL_LOCK_TIME;
        Integer validFailTime = CommConstants.MESSAGE.MESSAGE_VALID_FAIL_COUNT;
        String validFailKey = CommConstants.MESSAGE.MESSAGE_VALID_FAIL_TIME_MAP + ":" + type + ":" + phoneNum;
        Integer validFailTimes = redisCache.getCacheObject(validFailKey);
        if (validFailTimes != null && validFailTimes.intValue() >= validFailTime) {
            //移除无用的数据
            if (bl) {
                redisCache.deleteObject(key);
            }
            throw new ServiceException("连续" + validFailTime + "次输入错误验证码，账号锁定，请" + (messageValidFailLockTime / 60) + "分钟后重试");
        }
        try {
            if (!bl) {
                log.info("验证码不存在或已使用");
                throw new ServiceException("无效验证码");
            } else {
                String redisV = redisCache.getCacheObject(key);
                if (StringUtils.isNotBlank(redisV)) {
                    JSONObject messageOb = JSONObject.parseObject(redisV);
                    String validCode = messageOb.getString("validCode");
                    String timeCode = messageOb.getString("timeCode");
                    if (messageCode.equals(validCode)) {
                        String nowTime = sf.format(new Date());
                        if (DateUtils.timeDiff(timeCode, nowTime, CommConstants.MESSAGE.MESSAGE_MAP_TIME + "")) {
                            //移除无用的数据
                            if (isValidatedDelete) {
                                redisCache.deleteObject(key);
                            }
                            return true;
                        } else {
                            //移除无用的数据
                            redisCache.deleteObject(key);
                            log.info("用户" + phoneNum + "输入的验证码已过期");
                            throw new ServiceException("验证码已失效");
                        }
                    } else {
                        throw new ServiceException("验证码不正确");
                    }
                } else {
                    throw new ServiceException("验证码不存在或已使用");
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public CommonResult getMessageCode(MessageSendVo messageSendVo) {
        int type = messageSendVo.getType();
        String account = messageSendVo.getAccount();

        if (StringUtils.isEmpty(account)) {
            throw new ServiceException("账号不能为空");
        }
        SysUser sysUser = sysUserService.selectUserByUserName(account);
        if (sysUser == null) {
//            throw new ServiceException("短信验证码已经下发，如未收到请检测账号是否正确");
            return ResponseGenerator.genFailResult("短信发送失败");
        }
        String phoneNum = sysUser.getPhonenumber();
        if (StringUtils.isNotEmpty(messageSendVo.getPhoneNum())
                && !messageSendVo.getPhoneNum().equals(phoneNum)
                && messageSendVo.getType() == 3) {
            // 修改手机号
            phoneNum = messageSendVo.getPhoneNum();
        }
        if (ObjectUtil.isEmpty(phoneNum)) {
//            throw new ServiceException("短信验证码已经下发，如未收到请检测账号是否正确");
            return ResponseGenerator.genFailResult("短信发送失败");
        }

        // 连续输错验证码次数校验
        Integer messageValidFailLockTime = CommConstants.MESSAGE.MESSAGE_VALID_FAIL_LOCK_TIME;
        Integer validFailTime = CommConstants.MESSAGE.MESSAGE_VALID_FAIL_COUNT;
        String validFailKey = CommConstants.MESSAGE.MESSAGE_VALID_FAIL_TIME_MAP + ":" + type + ":" + phoneNum;
        Integer validFailTimes = redisCache.getCacheObject(validFailKey);
        if (validFailTimes != null && validFailTimes.intValue() >= validFailTime) {
            throw new ServiceException("账号已锁定，请" + (messageValidFailLockTime / 60) + "分钟后重试");
        }

        String key = CommConstants.MESSAGE.MESSAGE_MAP + ":" + type + ":" + phoneNum;
        /*
            工信部远程检查短信炸弹标准：
            关于远程工作细节现通知如下：
            1.短信炸弹问题
            通报标准为：10条及以上/1分钟，20条及以上/2分钟，30条及以上/3分钟。达到上述三条标准任意一条，即构成短信炸弹。
            补充通报标准：根据系统实际业务情况，若无大规模短信验证码需求，
            单个号码一天可接收50条及以上短信也可构成短信炸弹。（此条不适用于邮箱炸弹）
        */
        // 根据工信部要求，当天某个账号短信次数限制调整为：当天某个手机号短信次数限制，与短信类型type无关
        String keySendMaxOneDay = CommConstants.MESSAGE.MESSAGE_MAP_SEND_MAX_ONE_DAY + ":" + phoneNum;
        String keySendMaxInterval = CommConstants.MESSAGE.MESSAGE_MAP_SEND_MAX_INTERVAL + ":" + type + ":" + phoneNum;

        try {
            if (redisCache.hasCacheKey(key)) {
                String redisV = redisCache.getCacheObject(key);
                if (StringUtils.isNotBlank(redisV)) {
                    JSONObject messageOb = JSONObject.parseObject(redisV);
                    String timeCode = messageOb.getString("timeCode");

                    String nowTime = DateUtils.getTime();
                    if (DateUtils.timeDiff(timeCode, nowTime, CommConstants.MESSAGE.MESSAGE_MAP_TIME + "")) {
                        throw new ServiceException("验证码还在有效期内,可再次使用");
                    }
                }
            }
            if (redisCache.hasCacheKey(keySendMaxOneDay)) {
                Integer temp = redisCache.getCacheObject(keySendMaxOneDay);
                if (temp >= maxOneDay) {
                    throw new ServiceException("您获取验证码次数已经超过单天可用总次数，请明天再试");
                }
            }
            if (redisCache.hasCacheKey(keySendMaxInterval)) {
                Integer temp = redisCache.getCacheObject(keySendMaxInterval);
                if (temp >= maxInterval) {
                    throw new ServiceException("您获取验证码太过频繁，请稍后再试");
                }
            }
            try {
                String sixNum = RandomUtil.randomNumbers(6);
                String content = "您的验证码为" + sixNum + "，本验证码5分钟内有效，感谢您的使用！";
                CommonResult sendResult = sendMessage(phoneNum, content, messageSendVo);
                if (sendResult == null || !sendResult.isSuccess()) {
                    return sendResult;
                }

                JSONObject messageOb = new JSONObject();
                messageOb.put("validCode", sixNum);
                messageOb.put("timeCode", DateUtils.getTime());
                redisCache.setCacheObject(key, messageOb.toJSONString(), CommConstants.MESSAGE.MESSAGE_MAP_TIME, TimeUnit.SECONDS);

                //设置当天总次数
                if (redisCache.hasCacheKey(keySendMaxOneDay)) {
                    redisCache.increment(keySendMaxOneDay, 1);
                } else {
                    redisCache.setCacheObject(keySendMaxOneDay, 1, DateUtils.getRemainSecondsOneDay(), TimeUnit.SECONDS);
                }

                //设置当天总次数
                if (redisCache.hasCacheKey(keySendMaxInterval)) {
                    redisCache.increment(keySendMaxInterval, 1);
                } else {
                    redisCache.setCacheObject(keySendMaxInterval, 1, intervalTime, TimeUnit.SECONDS);
                }
                // 短信记录
                this.saveSmsSendRecord(phoneNum, "0", 0, content, messageSendVo);
                return ResponseGenerator.genSuccessResult();
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>", e);
                throw new ServiceException("调用服务获取验证码接口异常");
            }
        } catch (ServiceException ex) {
            log.error(">>>>>>>>>>>>>", ex);
            throw new ServiceException(ex.getMessage());
        } catch (Exception e) {
            throw new ServiceException("调用服务获取验证码接口异常");
        }
    }

    private void saveSmsSendRecord(String phone, String status, Integer port, String content, MessageSendVo messageSendVo) {
        BizMessageSendRecord message = new BizMessageSendRecord();
        message.setPhone(phone);
        message.setStatus(status);
        message.setPort(port);
        message.setType(messageSendVo.getType());
        message.setAppCode(messageSendVo.getAppCode());
        message.setContent(content);
        message.setCreateBy("system");
        message.setCreateTime(DateUtils.getNowDate());

        bizMessageSendRecordService.insertBizMessageSendRecord(message);
    }


    @Override
    public CommonResult sendMessage(String phoneNum, String content, MessageSendVo messageSendVo) {
        long result = -10000;
        String userid = msgAccount;
        int msgFmt = 8;
        String password = msgPassword;
        String message = msgPrefix + content;
        String mobile = phoneNum;
        String charset = msgFmt == 15 ? "gbk" : "utf-8";
        String ext = "";
        try {
            message = URLEncoder.encode(message, charset);
        } catch (UnsupportedEncodingException e) {
            log.error("调用服务发送短信失败,原因:" + e.getMessage());
        }
        String param = String.format("userid=%s&passwordMd5=%s&msg_fmt=%s&message=%s&mobile=%s&ext=", userid, password, msgFmt, message, mobile, ext);
        // 调用接口，注意：请求内容类型content-type为application/x-www-form-urlencoded
        String tempResult = HttpRequest.post(msgUrl).body(param).execute().body();

        log.info("[sendMessage]====>>>>>>>>result:{}", tempResult);
        if (StringUtils.isNotEmpty(tempResult)) {
            result = Long.valueOf(tempResult);
        }
        if (result > 0) {
            return ResponseGenerator.genSuccessResult();
        } else {
            log.error("调用服务发送短信失败 => param:{},原因:" + tempResult, param);
            return ResponseGenerator.genFailResult("短信发送失败，原因:" + tempResult);
        }
    }
}
