package com.eshore.common.core.domain.model;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户身份权限
 *
 * @author eshore
 */
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    private Integer platform;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 关联的数据id
     */
    private Long associationDataId;

    /**
     * 用户信息
     */
    private SysUser user;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginUser() {
    }

    public LoginUser(SysUser user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long userId, Long deptId, SysUser user, Set<String> permissions) {
        this.userId = userId;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Integer platform,Long userId, Long deptId,String userType, SysUser user, Set<String> permissions) {
        this.platform = platform;
        this.userId = userId;
        this.deptId = deptId;
        this.user = user;
        this.userType = userType;
        this.permissions = permissions;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getPlatform() {
        if (ObjectUtil.isNotEmpty(user) && ObjectUtil.isNotEmpty(user.getLoginPlatformType())) {
            return user.getLoginPlatformType();
        }
        return platform;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 获取用户类型code
     * @return
     */
    public String getUserType() {
        if (ObjectUtil.isNotEmpty(user) && ObjectUtil.isNotEmpty(user.getUserType())) {
            return user.getUserType();
        }
        return userType;
    }

    /**
     * 获取用户类型枚举值
     * @return
     */
    public UserType getUserTypeEnum(){
        if (ObjectUtil.isNotEmpty(user) && ObjectUtil.isNotEmpty(user.getUserType())) {
            return UserType.findByCode(user.getUserType());
        }
        return UserType.findByCode(userType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 获取关联的数据id，如 安全员id
     * @return
     */
    public Long getAssociationDataId() {
        if (ObjectUtil.isNotEmpty(user) && ObjectUtil.isNotEmpty(user.getAssociationDataId())) {
            return user.getAssociationDataId();
        }
        return associationDataId;
    }

    public void setAssociationDataId(Long associationDataId) {
        this.associationDataId = associationDataId;
    }
}
