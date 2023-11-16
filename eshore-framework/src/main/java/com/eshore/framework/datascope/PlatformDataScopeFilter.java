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
public class PlatformDataScopeFilter extends CommonDataScope implements IDataScopeFilter {


    @Override
    public void doFilter(DataScopeFilterParams params) {
        SysUser user = params.getUser();
        JoinPoint joinPoint = params.getJoinPoint();
        String tableAlias = params.getTableAlias();
        StringBuilder sqlString = new StringBuilder();

        Integer platformType = super.getPlatformType(user, joinPoint);

        if (ObjectUtil.isEmpty(platformType)) return;
        String templateSql = "";
        // 没有别名就不加了
        if (StringUtils.isEmpty(tableAlias)) {
            if (params.isFindInSet()) {
                templateSql = StringUtils.format(" FIND_IN_SET({},platform_type) ", String.valueOf(platformType));
            } else {
                templateSql = StringUtils.format(" platform_type = {} ", platformType);
            }
        } else {
            if (params.isFindInSet()) {
                templateSql = StringUtils.format(" FIND_IN_SET({},{}.platform_type) ", String.valueOf(platformType), tableAlias);
            } else {
                templateSql = StringUtils.format(" {}.platform_type = {} ", tableAlias, platformType);
            }
        }
        sqlString.append(templateSql);

        super.putDataScopeSql(joinPoint, sqlString.toString(), null);
    }
}
