package com.eshore.framework.web.service;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.constant.Constants;
import com.eshore.common.constant.RedisConstants;
import com.eshore.common.core.domain.model.LoginBody;
import com.eshore.common.core.domain.model.LoginResponseBody;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.exception.user.UserPasswordNotMatchException;
import com.eshore.common.utils.MessageUtils;
import com.eshore.domain.model.vo.common.MessageSendVo;
import com.eshore.framework.auth.PhoneAuthenticationToken;
import com.eshore.framework.manager.AsyncManager;
import com.eshore.framework.manager.factory.AsyncFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName PhoneLogin
 * @Description 手机验证码登录
 * @Author jianlin.liu
 * @Date 2022/9/15
 * @Version 1.0
 **/
@Slf4j
@Service
public class PhoneCodeLogin extends SysLoginService implements ILogin {
    @Value("${login.errorMaxTimes}")
    private int errorMaxTimes;

    @Value("${login.errorForbiddenTime}")
    private int errorForbiddenTime;

    @Override
    public LoginResponseBody login(LoginBody loginBody) {
        if (ObjectUtil.isEmpty(loginBody.getPhone())) {
            throw new ServiceException("手机号不能为空");
        }
        if (ObjectUtil.isEmpty(loginBody.getMessageCode())) {
            throw new ServiceException("短信验证码不能为空");
        }
        if (loginCheckConfig.isCheckMessageCode() && loginBody.getLoginWay().intValue() == 2) {
            //判断短信验证码是否存在
            MessageSendVo messageSendVo = new MessageSendVo();
            messageSendVo.setPhoneNum(loginBody.getPhone());
            messageSendVo.setType(MessageSendVo.TYPE_LOGIN);
            messageSendVo.setMessageCode(loginBody.getMessageCode());
            messageSendVo.setAppCode(loginBody.getAppCode());
            boolean isValidate = messageService.validMessageCode(messageSendVo, true);
            if (!isValidate) {
                throw new ServiceException("短信验证码验证失败");
            }
        }
        String key = RedisConstants.LOGIN_ERROR_KEY_PREFIX + RedisConstants.LOGIN_ERROR_TIMES + loginBody.getPhone();
        Integer loginErrorTimes = redisCache.getCacheObject(key);
        if (loginErrorTimes != null && loginErrorTimes.intValue() >= errorMaxTimes) {
            throw new ServiceException("连续" + errorMaxTimes + "次登录失败，手机号已被锁定，请" + (errorForbiddenTime / 60) + "分钟后重试");
        }
        return loginByPhone(loginBody.getPlatform(), loginBody.getPhone(), key);
    }

    /**
     * 手机验证码登录验证
     *
     * @param phone 手机号
     * @return 结果
     */
    private LoginResponseBody loginByPhone(Integer loginPlatform, String phone, String key) {
        // 用户验证
        Authentication authentication = null;
        LoginUser loginUser = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new PhoneAuthenticationToken(phone));
            loginUser = (LoginUser) authentication.getPrincipal();
            // 检查登录平台是否与用户类型相匹配
            super.checkPlatForm(loginPlatform, loginUser);
        } catch (Exception e) {
            Integer loginErrorTimes = redisCache.getCacheObject(key);
            if (loginErrorTimes != null) {
                loginErrorTimes++;
            } else {
                loginErrorTimes = 1;
            }
            redisCache.setCacheObject(key, loginErrorTimes, errorForbiddenTime, TimeUnit.SECONDS);
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException("登录失败");
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));

        recordLoginInfo(loginUser.getUserId());
        // 生成token
        String token = tokenService.createToken(loginUser);
        if (loginCheckConfig.isLoginSingle()) {
            // 开启了单用户登录
            log.debug(">>>>>>>>>>开启单点登录：username:{},browser:{},token:{}", loginUser.getUsername(), loginUser.getBrowser(), loginUser.getToken());
            // userOnlineService.removeLoginOnUser(loginUser);
            // 踢掉之前登录token
            userOnlineService.checkLoginOnUser(loginUser);
        }
        redisCache.setCacheObject(key, 0, errorForbiddenTime, TimeUnit.SECONDS);

        return super.getLoginResponse(token, loginUser);
    }
}
