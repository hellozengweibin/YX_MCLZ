package com.eshore.framework.web.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.config.LoginCheckConfig;
import com.eshore.common.constant.Constants;
import com.eshore.common.core.domain.entity.SysRole;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.enums.UserStatus;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户验证处理
 *
 * @author eshore
 */
@Service("UserDetailsByUserNameServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Resource
    protected LoginCheckConfig loginCheckConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName = username;
        String platformType = null;
        if (loginCheckConfig.isCheckPlatform() || username.contains(Constants.LOGIN_SEPARATOR)) {
            String[] split = username.split(Constants.LOGIN_SEPARATOR);
            userName = split[0];
            platformType = split[1];

        }
        Integer loginPlatform = platformType == null ? null : Integer.valueOf(platformType);
        log.info(">>>>>>>>>>username:{} => start login platform:{}", username,
                platformType != null ? PlatformType.findByCode(loginPlatform).getName() : "");

        SysUser user = userService.selectUserByUserName__(userName, loginPlatform);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("用户名或密码错误");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("您的账号：" + username + " 已停用");
        }
        // 角色判断
        List<SysRole> roles = user.getRoles();
        if (CollectionUtil.isEmpty(roles)) throw new ServiceException("当前账号未绑定角色");
        roles = roles.stream().filter(item -> "0".equals(item.getStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(roles)) throw new ServiceException("当前账号绑定的角色已被停用");

        log.debug(">>>>>>>loginUser:{} => userType:{}", user.getUserName(),user.getUserType());
        if (loginCheckConfig.isCheckPlatform()) this.validateByUserType(user, loginPlatform);
        user.setLoginPlatformType(loginPlatform);
        return createLoginUser(loginPlatform, user);
    }


    public UserDetails createLoginUser(Integer loginPlatform, SysUser user) {
        return new LoginUser(loginPlatform, user.getUserId(), user.getDeptId(), user.getUserType(), user, permissionService.getMenuPermission(user));
    }

    /**
     * 校验用户类型
     *
     * @param user
     */
    public void validateByUserType(SysUser user, Integer loginPlatformType) {
        if (ObjectUtil.isEmpty(loginPlatformType)) return;
        boolean flag = false;
        String errMsg = null;
        String errPrefix = "登录失败，";
        String userPlatformType = user.getPlatformType();
        // 用户的类型与登录平台检测
        if (loginCheckConfig.isCheckPlatform() && !SecurityUtils.isAdmin(user.getUserId())) {
            if (StringUtils.isEmpty(userPlatformType)) {
                throw new ServiceException(errPrefix + "当前账号无平台权限");
            }
            if (!user.getPlatformType().contains(String.valueOf(loginPlatformType))) {
                throw new ServiceException(errPrefix + "当前账号无平台权限");
            }
        }
        if (flag && StringUtils.isNotEmpty(errMsg)) {
            throw new ServiceException(errMsg);
        }
    }

}
