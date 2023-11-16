package com.eshore.framework.aspectj;

import com.eshore.common.annotation.DataScope;
import com.eshore.common.annotation.DataScopes;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.domain.base.BaseEntity;
import com.eshore.framework.datascope.DataScopeFilter;
import com.eshore.framework.datascope.DataScopeFilterParams;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 数据过滤处理
 *
 * @author eshore
 */
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 层级数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 层级及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScopes controllerDataScope) throws Throwable {
        clearDataScope(point);
        for (DataScope dataScope : controllerDataScope.value()) {
            handleDataScope(point, dataScope);
        }

    }

    @Before("@annotation(controllerDataScope)")
    public void doBefore1(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (!controllerDataScope.skipAdmin() || (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin())) {
                DataScopeFilterParams params = new DataScopeFilterParams();
                params.setDeptAlias(controllerDataScope.deptAlias());
                params.setJoinPoint(joinPoint);
                params.setDataScopeType(controllerDataScope.type());
                params.setUser(currentUser);
                params.setUserAlias(controllerDataScope.userAlias());
                params.setTableAlias(controllerDataScope.tableAlias());
                params.setFindInSet(controllerDataScope.findInSet());
                params.setColumn(controllerDataScope.column());
                if (StringUtils.isEmpty(params.getColumn())) {
                    params.setColumn(controllerDataScope.type().getColumnName());
                }
                DataScopeFilter.getInstance().doFilter(params);
            }
        }
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
