package com.eshore.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.common.annotation.DataScope;
import com.eshore.common.core.domain.entity.SysRole;
import com.eshore.common.enums.DataScopeType;
import com.eshore.domain.base.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author eshore
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @DataScope(type = DataScopeType.PLATFORM,tableAlias = "r")
    List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @DataScope(type = DataScopeType.PLATFORM,tableAlias = "r")
    List<SysRole> selectRoleAll();

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    List<Long> selectRoleListByUserId(Long userId);

    /**
     * 根据平台类型查询角色列表
     * @param platformTypes
     * @return
     */
    @DataScope(type = DataScopeType.CREATER,tableAlias = "r")
    List<SysRole> selectRoleListByPlatformType(@Param("param") BaseEntity baseEntity,
                                               @Param("platformTypes") List<Integer> platformTypes);

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserName(String userName);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    SysRole checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    SysRole checkRoleKeyUnique(String roleKey);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(SysRole role);

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SysRole role);

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] roleIds);

    SysRole selectRoleByRoleKey(@Param("roleKey") String roleKey, @Param("platformType") String platformType);
}
