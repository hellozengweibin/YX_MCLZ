package com.eshore.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.common.annotation.DataScope;
import com.eshore.common.core.domain.entity.SysMenu;
import com.eshore.common.enums.DataScopeType;
import com.eshore.domain.base.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author eshore
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @DataScope(type = DataScopeType.PLATFORM)
    List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    @DataScope(type = DataScopeType.PLATFORM, tableAlias = "m")
    List<String> selectMenuPerms(BaseEntity baseEntity);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @DataScope(type = DataScopeType.PLATFORM, tableAlias = "m")
    List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 根据用户ID查询权限
     *
     * @param userId       用户ID
     * @param platformType 平台类型
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(@Param("userId") Long userId, @Param("platformType") Integer platformType);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    @DataScope(type = DataScopeType.PLATFORM, tableAlias = "m", skipAdmin = false)
    List<SysMenu> selectMenuTreeAll(BaseEntity baseEntity);

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId, @Param("platformType") Integer platformType);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId, @Param("platformType") Integer platformType);
}
