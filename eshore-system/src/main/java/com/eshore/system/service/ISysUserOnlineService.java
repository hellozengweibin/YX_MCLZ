package com.eshore.system.service;

import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.system.domain.SysUserOnline;

/**
 * 在线用户 服务层
 *
 * @author eshore
 */
public interface ISysUserOnlineService {
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);

    /**
     * 同一平台的同一个用户名只允许一个浏览器登录
     *
     * @param loginUser 登录用户信息
     * @return
     */
    public void checkLoginOnUser(LoginUser loginUser);

    /**
     * 检查登录用户
     * 统一个用户名只能在同一个浏览器登录，剔除在别的地方或别的浏览器登录的账号
     * @param loginUser
     */
    void removeLoginOnUser(LoginUser loginUser);

}
