package com.eshore.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.annotation.Log;
import com.eshore.common.constant.UserConstants;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.domain.entity.SysRole;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.page.TableDataInfo;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.domain.model.dto.UpdatePlatformBody;
import com.eshore.system.service.ISysRoleService;
import com.eshore.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author eshore
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "用户信息模块")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    @ApiOperation("导出用户列表")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public CommonResult<String> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    @ApiOperation("导出用户列表")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    @ApiOperation("根据用户编号获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Long", dataTypeClass = Long.class, required = true),
    })
    public CommonResult<UserInfoVO> getUserInfo(@PathVariable(value = "userId", required = false) Long userId,
                                                @RequestParam(value = "platformTypes", required = false) String platformTypes) {
        userService.checkUserDataScope(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            if (ObjectUtil.isAllNotEmpty(sysUser, sysUser.getRoleIds())) {
                userInfoVO.setRoleIds(Arrays.stream(sysUser.getRoleIds()).collect(Collectors.toList()));
            }
            userInfoVO.setData(sysUser);
        }
        return success(userInfoVO);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增用户")
    public CommonResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        if (ObjectUtil.isEmpty(user.getPlatformType()))
            user.setPlatformType(String.valueOf(SecurityUtils.getLoginUserCurrentPlatform().getCode()));
        return toResult(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改用户")
    public CommonResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        if (ObjectUtil.isEmpty(user.getPlatformType()))
            user.setPlatformType(String.valueOf(SecurityUtils.getLoginUserCurrentPlatform().getCode()));
        return toResult(userService.updateUser(user, true, true));
    }

    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "修改平台类型", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updatePlatform")
    @ApiOperation("修改平台类型")
    public CommonResult<?> updatePlatform(@Validated @RequestBody UpdatePlatformBody updatePlatformBody) {
        return toResult(userService.updateRolePlatform(updatePlatformBody));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    @ApiOperation("删除用户")
    public CommonResult remove(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("当前用户不能删除");
        }
        return toResult(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    @ApiOperation("重置密码")
    public CommonResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toResult(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("状态修改")
    public CommonResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toResult(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    @ApiOperation("根据用户编号获取授权角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<AuthRoleVo> authRole(@PathVariable("userId") Long userId) {
        AuthRoleVo authRoleVo = new AuthRoleVo();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        authRoleVo.setUser(user);
        authRoleVo.setRoles(SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return success(authRoleVo);
    }

    @Data
    public static class AuthRoleVo {
        private SysUser user;
        private List<SysRole> roles;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    @ApiOperation("用户授权角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Long", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(name = "roleIds", value = "角色id数组", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    @Data
    @Accessors(chain = true)
    public static class UserInfoVO {
        private List<SysRole> roles;
        private List<Long> roleIds;
        private SysUser data;
    }

    /**
     * 查询故障派单运维人员
     */
    @PreAuthorize("@ss.hasPermi('system:user:queryOptUser')")
    @GetMapping("/queryOptUser")
    @ApiOperation("查询故障派单运维人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "运维单位id", dataType = "Long", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(name = "devId", value = "设备id", dataType = "Long", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(name = "devType", value = "设备类型", dataType = "Integer", dataTypeClass = Integer.class, required = true)
    })
    public CommonResult queryOptUser(Long deptId, Long devId, Integer devType) {
        List<SysUser> list = userService.queryOptUser(deptId, devId, devType);
        return success(list);
    }
}
