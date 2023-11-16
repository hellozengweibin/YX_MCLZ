package com.eshore.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.constant.Constants;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.utils.StringUtils;
import com.eshore.system.domain.SysUserOnline;
import com.eshore.system.service.ISysUserOnlineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 在线用户 服务层处理
 *
 * @author eshore
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    @Resource
    private RedisCache redisCache;

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user) {
        if (StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUser())) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        if (StringUtils.isNotNull(user.getUser().getDept())) {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        return sysUserOnline;
    }

    /**
     * 同一平台的同一个用户名只允许一个浏览器登录
     *
     * @param loginUser 登录用户信息
     * @return
     */
    @Override
    public void checkLoginOnUser(LoginUser loginUser) {
        String keyPrefix = Constants.LOGIN_TOKEN_KEY + loginUser.getUserId() + ":";
        Collection<String> keys = redisCache.keys(keyPrefix + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty(loginUser.getUsername()) && StringUtils.isNotNull(user.getUser())) {
                if (StringUtils.equals(loginUser.getUsername(), user.getUsername())) {
                    userOnlineList.add(selectOnlineByUserName(loginUser.getUsername(), user));
                }
            }
        }
        if (CollUtil.isNotEmpty(userOnlineList)) {
            for (SysUserOnline sysUserOnline : userOnlineList) {
//                if (!sysUserOnline.getBrowser().equals(loginUser.getBrowser()) && sysUserOnline.getPlatform().equals(loginUser.getPlatform())) {
//                    redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + sysUserOnline.getTokenId());
//                }
                if (loginUser.getPlatform().equals(sysUserOnline.getPlatform())) {
                    redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + sysUserOnline.getTokenId());
                }
            }
        }
    }

    @Override
    public void removeLoginOnUser(LoginUser loginUser) {
        if (ObjectUtil.isEmpty(loginUser)) return;
        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + loginUser.getUserId() + ":" + "*");
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (ObjectUtil.isNotEmpty(user.getUsername()) && ObjectUtil.isNotEmpty(user.getUser())) {
                if (user.getPlatform().equals(String.valueOf(loginUser.getPlatform()))) {
                    redisCache.deleteObject(key);
                }
            }
        }
    }
}
