package com.eshore.framework.web.service;

import com.eshore.common.constant.Constants;
import com.eshore.common.constant.RedisConstants;
import com.eshore.common.core.domain.model.LoginBody;
import com.eshore.common.core.domain.model.LoginResponseBody;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.exception.user.UserPasswordNotMatchException;
import com.eshore.common.utils.MessageUtils;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.domain.model.vo.common.MessageSendVo;
import com.eshore.framework.manager.AsyncManager;
import com.eshore.framework.manager.factory.AsyncFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserNamePasswordLogin
 * @Description 账号密码登录
 * @Author jianlin.liu
 * @Date 2022/9/15
 * @Version 1.0
 **/
@Slf4j
@Service
public class UserNamePasswordLogin extends SysLoginService implements ILogin {
    @Value("${login.errorMaxTimes}")
    private int errorMaxTimes;

    @Value("${login.errorForbiddenTime}")
    private int errorForbiddenTime;

    @Override
    public LoginResponseBody login(LoginBody loginBody) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        Integer loginPlatform = loginBody.getPlatform();

        if (loginCheckConfig.isCheckMessageCode()) {
            // 校验短信验证码
            if (StringUtils.isNotEmpty(loginCheckConfig.getSuperMessageCode()) &&
                    !loginCheckConfig.getSuperMessageCode().equals(loginBody.getMessageCode())) {
                MessageSendVo messageSendVo = new MessageSendVo();
                messageSendVo.setAccount(loginBody.getUsername());
                // messageSendVo.setPhoneNum(loginBody.getPhone());
                messageSendVo.setType(MessageSendVo.TYPE_LOGIN);
                messageSendVo.setAppCode(loginBody.getAppCode());
                messageSendVo.setMessageCode(loginBody.getMessageCode());
                messageService.validMessageCode(messageSendVo, true);
            }
        }

        //判断多次登录失败，直接返回
        String key = RedisConstants.LOGIN_ERROR_KEY_PREFIX + RedisConstants.LOGIN_ERROR_TIMES + username;
        Integer loginErrorTimes = redisCache.getCacheObject(key);
        if (loginErrorTimes != null && loginErrorTimes.intValue() >= errorMaxTimes) {
            throw new ServiceException("连续" + errorMaxTimes + "次登录失败，账号锁定，请" + (errorForbiddenTime / 60) + "分钟后重试");
        }
        return getTokenByUserNamePassword(loginPlatform, username, password, key);
    }

    /**
     * 账号密码登录
     *
     * @param username
     * @param password
     * @return
     */
    private LoginResponseBody getTokenByUserNamePassword(Integer loginPlatform, String username, String password, String key) {
        // 用户验证
        Authentication authentication = null;
        LoginUser loginUser = null;
        try {
            String userNameAndPlatform = username + Constants.LOGIN_SEPARATOR + loginPlatform;
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userNameAndPlatform, password));

            loginUser = (LoginUser) authentication.getPrincipal();
            // 检查登录平台是否与用户类型相匹配
            super.checkPlatForm(loginPlatform, loginUser);
        } catch (ServiceException ex) {
            throw new ServiceException(ex.getMessage());
        } catch (Exception e) {
            Integer loginErrorTimes = redisCache.getCacheObject(key);
            if (loginErrorTimes != null) {
                loginErrorTimes++;
            } else {
                loginErrorTimes = 1;
            }
            redisCache.setCacheObject(key, loginErrorTimes, errorForbiddenTime, TimeUnit.SECONDS);
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));

        recordLoginInfo(loginUser.getUserId());

        if (SecurityUtils.isAdmin(loginUser.getUserId())) {
            loginUser.setPlatform(loginPlatform);
        }

        if (loginCheckConfig.isLoginSingle()) {
            // 开启了单用户登录
            log.debug(">>>>>>>>>>开启单点登录：username:{},browser:{},token:{}", username, loginUser.getBrowser(), loginUser.getToken());
            // 踢掉之前登录token
            userOnlineService.checkLoginOnUser(loginUser);
            // userOnlineService.removeLoginOnUser(loginUser);
        }
        redisCache.setCacheObject(key, 0, errorForbiddenTime, TimeUnit.SECONDS);
        // 生成token
        String token = tokenService.createToken(loginUser);
        return super.getLoginResponse(token, loginUser);
    }

}
