package com.eshore.framework.datascope;

import com.eshore.common.core.domain.entity.SysRole;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;

/**
 * 部门数据过滤
 *
 * @author shanglangxin
 * @since 2023/5/12 9:42
 */
public class DeptDataScopeFilter extends CommonDataScope implements IDataScopeFilter {

    @Override
    public void doFilter(DataScopeFilterParams params) {
        SysUser user = params.getUser();
        String deptAlias = params.getDeptAlias();
        JoinPoint joinPoint = params.getJoinPoint();
        String userAlias = params.getUserAlias();
        StringBuilder sqlString = new StringBuilder();

        for (SysRole role : user.getRoles()) {
            String dataScope = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                // 没有别名就不加了
                if (StringUtils.isEmpty(deptAlias)) {
                    sqlString.append(StringUtils.format(
                            " OR dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", role.getRoleId()));
                } else {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                            role.getRoleId()));
                }
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                // 没有别名就不加了
                if (StringUtils.isEmpty(deptAlias)) {
                    sqlString.append(StringUtils.format(" OR dept_id = {} ", user.getDeptId()));
                } else {
                    sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
                }
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                // 没有别名就不加了
                if (StringUtils.isEmpty(deptAlias)) {
                    sqlString.append(StringUtils.format(
                            " OR dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            user.getDeptId(), user.getDeptId()));
                } else {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            deptAlias, user.getDeptId(), user.getDeptId()));
                }

            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }

        super.putDataScopeSql(joinPoint, sqlString.toString(), 4);
    }
}
