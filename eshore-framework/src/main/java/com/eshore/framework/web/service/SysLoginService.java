package com.eshore.framework.web.service;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.business.service.common.IMessageService;
import com.eshore.common.config.LoginCheckConfig;
import com.eshore.common.constant.Constants;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginResponseBody;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.exception.user.CaptchaException;
import com.eshore.common.exception.user.CaptchaExpireException;
import com.eshore.common.utils.DateUtils;
import com.eshore.common.utils.MessageUtils;
import com.eshore.common.utils.ServletUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.ip.IpUtils;
import com.eshore.framework.manager.AsyncManager;
import com.eshore.framework.manager.factory.AsyncFactory;
import com.eshore.system.service.ISysConfigService;
import com.eshore.system.service.ISysUserOnlineService;
import com.eshore.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author eshore
 */
// @Component
@Slf4j
public class SysLoginService {
    @Autowired
    protected TokenService tokenService;
    @Resource
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected RedisCache redisCache;
    @Autowired
    protected ISysUserService userService;
    @Autowired
    protected ISysConfigService configService;
    @Resource
    protected ISysUserOnlineService userOnlineService;
    @Resource
    protected LoginCheckConfig loginCheckConfig;

    @Autowired
    protected IMessageService messageService;


    /**
     * 检查登录平台是否与用户类型匹配
     * 管理端只能从管理端登录，客户端账号只能从客户端登录，装维小程序只能从小程序登录
     *
     * @param platform
     * @param loginUser
     */
    protected void checkPlatForm(int platform, LoginUser loginUser) {
        if (!loginCheckConfig.isCheckPlatform()) {
            log.debug(">>>>>>>>>>>not check platform");
            return;
        }
        Integer userPlatform = loginUser.getPlatform();
        if (ObjectUtil.isEmpty(userPlatform)) {
            log.warn(">>>>>>>>>>>>user:{} platform is null", loginUser.getUsername());
            return;
        }

        PlatformType platformType = PlatformType.findByCode(platform);
        if (ObjectUtil.isEmpty(platformType)) {
            throw new ServiceException("登录失败，平台类型参数错误");
        }

        if (platform != userPlatform.intValue()) {
            log.error(">>>>>>>>>>>用户[{}] => 用户平台[{}] 与 登录平台[{}]不匹配 ===> not allow login",
                    loginUser.getUsername(), loginUser.getPlatform(), platformType.getName());
            // 强制下线
            userOnlineService.removeLoginOnUser(loginUser);
            throw new ServiceException(String.format("登录失败，此账号无[%s]平台权限", platformType.getName()));
        }
        loginUser.setPlatform(platform);
    }

    /**
     * 获取登录响应
     *
     * @param token
     * @param loginUser
     * @return
     */
    protected LoginResponseBody getLoginResponse(String token, LoginUser loginUser) {
        LoginResponseBody loginResponseBody = new LoginResponseBody();
        loginResponseBody.setToken(token);
        loginResponseBody.setUserName(loginUser.getUsername());
        SysUser user = loginUser.getUser();
        if (ObjectUtil.isNotEmpty(user)) {
            loginResponseBody.setUserType(user.getUserType());
            // loginResponseBody.setInit(user.getInitFlag());
            //判断密码是否已到期
            boolean isExpire = false;
            // if (ObjectUtil.isNotEmpty(user.getInitFlag()) && user.getInitFlag() == 0) {
            //     // 初始账号时直接是到期
            //     isExpire = true;
            // } else if (ObjectUtil.isNotEmpty(user.getPasswordExpireTime())) {
            //     // 过期 2022/09/15
            //     isExpire = (user.getPasswordExpireTime().getTime() <= DateUtils.getNowDate().getTime());
            // }
            loginResponseBody.setPwdExpire(isExpire);
        }
        return loginResponseBody;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    protected void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    protected void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
