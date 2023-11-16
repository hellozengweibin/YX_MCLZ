package com.eshore.framework.datascope;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

/**
 * 客户权限过滤
 *
 * @author jianlin.liu
 * @since 2023/6/30 9:42
 */
@Slf4j
public class CreaterDataScopeFilter extends CommonDataScope implements IDataScopeFilter {


    @Override
    public void doFilter(DataScopeFilterParams params) {
        SysUser user = params.getUser();
        JoinPoint joinPoint = params.getJoinPoint();
        String tableAlias = params.getTableAlias();

        Integer platformType = super.getPlatformType(user, joinPoint);

        String platformSql = "", deptSql = "";
        if (ObjectUtil.isNotEmpty(platformType)) {
            platformSql = StringUtils.format(" FIND_IN_SET({},u.platform_type) ", platformType);
        }
        if (ObjectUtil.isNotEmpty(user.getDeptId())) {
            Long deptId = user.getDeptId();
            deptSql = StringUtils.format(" AND u.dept_id IN ( SELECT dept_id FROM sys_dept WHERE (dept_id = {} OR FIND_IN_SET( {} , ancestors ) )  ", deptId, deptId);
            if (StringUtils.isNotEmpty(platformSql)) deptSql = deptSql + " AND " + platformSql;
            deptSql += " ) ";
        }

        // 创建人字段名，为空默认为create_by
        String columnName = StringUtils.isEmpty(params.getColumn()) ? "create_by" : params.getColumn();
        columnName = StringUtils.isEmpty(tableAlias) ? columnName : (StringUtils.format("{}.{}", tableAlias, columnName));
        String templateSql = StringUtils.format(" {} IN (SELECT DISTINCT user_name FROM sys_user u WHERE {} {} ) ",
                columnName, platformSql, deptSql);

        super.putDataScopeSql(joinPoint, templateSql, null);
    }
}
