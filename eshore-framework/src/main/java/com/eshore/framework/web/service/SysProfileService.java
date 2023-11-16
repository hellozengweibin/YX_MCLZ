package com.eshore.framework.web.service;

import com.eshore.business.service.common.IMessageService;
import com.eshore.common.constant.Constants;
import com.eshore.common.constant.UserConstants;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.exception.user.CaptchaException;
import com.eshore.common.exception.user.CaptchaExpireException;
import com.eshore.common.utils.*;
import com.eshore.common.utils.ip.IpUtils;
import com.eshore.domain.model.dto.UpdatePasswordDto;
import com.eshore.domain.model.vo.common.MessageSendVo;
import com.eshore.framework.manager.AsyncManager;
import com.eshore.framework.manager.factory.AsyncFactory;
import com.eshore.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录校验方法
 *
 * @author eshore
 */
@Component
public class SysProfileService {

    @Value("${login.checkMessageCode:true}")
    protected boolean loginCheckMessageCode;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    protected IMessageService messageService;


    /**
     * 修改密码
     *
     * @param updatePasswordDto 修改密码入参
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updatePwd(UpdatePasswordDto updatePasswordDto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String userName = loginUser.getUsername();

        if (loginCheckMessageCode && (!UserConstants.MSG_CODE.equals(updatePasswordDto.getMessageCode()))) {
            // 短信验证码校验
            MessageSendVo messageSendDto = new MessageSendVo();
            messageSendDto.setMessageCode(updatePasswordDto.getMessageCode());
            messageSendDto.setAccount(userName);
            // 修改密码
            messageSendDto.setType(1);
            messageSendDto.setPlatform(updatePasswordDto.getPlatform());
            messageService.validMessageCode(messageSendDto, true);
        }

        String password = loginUser.getPassword();

        String checkPassword = PwdCheckUtil.checkPassword(userName, updatePasswordDto.getNewPassword());
        if (!UserConstants.VALID_PASSWORD.equals(checkPassword)) {
            throw new ServiceException(checkPassword);
        }

        if (!SecurityUtils.matchesPassword(updatePasswordDto.getOldPassword(), password)) {
            return ResponseGenerator.genFailResult("原密码不正确");
        }

        if (SecurityUtils.matchesPassword(updatePasswordDto.getNewPassword(), password)) {
            return ResponseGenerator.genFailResult("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(updatePasswordDto.getNewPassword())) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(updatePasswordDto.getNewPassword()));
            tokenService.setLoginUser(loginUser);
            return ResponseGenerator.genSuccessResult();
        }
        return ResponseGenerator.genFailResult("修改密码异常，请联系管理员");

    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
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
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
