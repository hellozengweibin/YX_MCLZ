package com.eshore.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.annotation.Log;
import com.eshore.common.constant.UserConstants;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.domain.entity.SysRole;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.core.page.TableDataInfo;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.common.utils.uuid.IdGenerateUtil;
import com.eshore.domain.model.dto.UpdatePlatformBody;
import com.eshore.framework.web.service.SysPermissionService;
import com.eshore.framework.web.service.TokenService;
import com.eshore.system.domain.SysUserRole;
import com.eshore.system.service.ISysRoleService;
import com.eshore.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息
 *
 * @author eshore
 */
@RestController
@RequestMapping("/system/role")
@Api(tags = "角色信息模块")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    @ApiOperation("获取角色信息列表")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @PostMapping("/export")
    @ApiOperation("导出角色列表")
    public void export(HttpServletResponse response, SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    @ApiOperation("根据角色编号获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<SysRole> getInfo(@PathVariable Long roleId) {
        // roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增角色")
    public CommonResult add(@Validated @RequestBody SysRole role) {
        PlatformType platform = SecurityUtils.getLoginUserCurrentPlatform();
        if (StringUtils.isEmpty(role.getRoleKey())) {
            String prefix = platform.getNameEn();
            if (ObjectUtil.isNotEmpty(role.getPlatformType())) {
                PlatformType platformType = PlatformType.findByCode(role.getPlatformType());
                prefix = ObjectUtil.isNotEmpty(platformType) ? platformType.getNameEn() : prefix;
            }
            role.setRoleKey(IdGenerateUtil.getInstance().nextId(prefix, "_", 2));
        }
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getUsername());
        if (ObjectUtil.isEmpty(role.getPlatformType()))
            role.setPlatformType(platform.getCode());
        return toResult(roleService.insertRole(role));
    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改保存角色")
    public CommonResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getUsername());
        if (ObjectUtil.isEmpty(role.getPlatformType()))
            role.setPlatformType(SecurityUtils.getLoginUserCurrentPlatform().getCode());

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return success();
        }
        return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "修改平台类型", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updatePlatform")
    @ApiOperation("修改平台类型")
    public CommonResult<?> updatePlatform(@Validated @RequestBody UpdatePlatformBody updatePlatformBody) {
        return toResult(roleService.updateRolePlatform(updatePlatformBody));
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    @ApiOperation("修改保存数据权限")
    public CommonResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role);
        return toResult(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("状态修改")
    public CommonResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role);
        role.setUpdateBy(getUsername());
        return toResult(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    @ApiOperation("删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "角色id数组", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult remove(@PathVariable Long[] roleIds) {
        return toResult(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionSelect")
    @ApiOperation("获取角色选择框列表")
    public CommonResult<List<SysRole>> optionSelect(@RequestParam(value = "platformTypes", required = false) String platformTypes) {
        return success(roleService.getRolesByPlatformType(platformTypes));
    }

    /**
     * 查询已分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    @ApiOperation("查询已分配用户角色列表")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    @ApiOperation("查询未分配用户角色列表")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    @ApiOperation("取消授权用户")
    public CommonResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toResult(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    @ApiOperation("批量取消授权用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "Long", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(name = "userIds", value = "用户id数组", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toResult(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    @ApiOperation("批量选择用户授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "Long", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(name = "userIds", value = "用户id数组", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(new SysRole(roleId));
        return toResult(roleService.insertAuthUsers(roleId, userIds));
    }
}
