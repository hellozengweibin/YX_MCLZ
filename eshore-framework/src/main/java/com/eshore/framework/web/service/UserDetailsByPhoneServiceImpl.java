package com.eshore.framework.web.service;


import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.enums.UserStatus;
import com.eshore.common.exception.base.BaseException;
import com.eshore.common.utils.StringUtils;
import com.eshore.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户手机号验证处理
 *
 * @author jianlin.liu
 * @version 1.0
 * @date 2022-09/15
 */
@Service("UserDetailsByPhoneServiceImpl")
public class UserDetailsByPhoneServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsByPhoneServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        SysUser user = userService.getUserByPhone(phone);
        if (StringUtils.isNull(user)) {
            log.info("手机号：{} 不存在.", phone);
            throw new UsernameNotFoundException("手机号：" + phone + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("手机号：{} 已被删除.", phone);
            throw new BaseException("对不起，您的账号：" + phone + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("手机号：{} 已被停用.", phone);
            throw new BaseException("对不起，您的账号：" + phone + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
