package com.eshore.web.controller.system;

import com.eshore.common.annotation.Log;
import com.eshore.common.constant.UserConstants;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.domain.TreeSelect;
import com.eshore.common.core.domain.entity.SysDept;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.utils.StringUtils;
import com.eshore.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author eshore
 */
@RestController
@RequestMapping("/system/dept")
@Api(tags = "部门信息模块")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation("获取部门列表")
    public CommonResult<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation("查询部门列表（排除节点）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/excludeOrgChild/{deptId}")
    @ApiOperation("查询部门列表（排除节点）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult excludeOrgChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        SysDept sd = new SysDept();
        sd.setPlatformType(PlatformType.TYPE_2.getCode() + "");
        List<SysDept> depts = deptService.selectDeptList(sd);
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    @ApiOperation("根据部门编号获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<SysDept> getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @ApiOperation("获取部门下拉树列表")
    public CommonResult<List<TreeSelect>> treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ApiOperation("加载对应角色部门列表树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<Map<String, Object>> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return success(ajax);
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增部门")
    public CommonResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getUsername());
        return toResult(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改部门")
    public CommonResult edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(getUsername());
        return toResult(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    @ApiOperation("删除部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<?> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return error("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return toResult(deptService.deleteDeptById(deptId));
    }


}
