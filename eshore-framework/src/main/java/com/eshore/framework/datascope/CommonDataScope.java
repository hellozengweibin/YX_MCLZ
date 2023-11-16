package com.eshore.framework.datascope;

import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.reflect.ReflectUtils;
import com.eshore.domain.base.BaseEntity;
import org.aspectj.lang.JoinPoint;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName CommonDataScope
 * @Description
 * @Author jianlin.liu
 * @Date 2023/7/11
 * @Version 1.0
 **/
public class CommonDataScope {

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
    protected static final String DATA_SCOPE = "dataScope";


    protected Integer getPlatformType(SysUser user, JoinPoint joinPoint) {
        Integer platformType = user.getLoginPlatformType();
        if (Objects.isNull(platformType)) {
            return null;
        }
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Object fieldValue = ReflectUtils.getFieldValue(arg, "platformType");
            if (fieldValue != null) {
                platformType = Integer.valueOf(String.valueOf(fieldValue));
                // 平台类型校验
                if (StringUtils.isNotEmpty(user.getPlatformType()) &&
                        !user.getPlatformType().contains(String.valueOf(fieldValue))) {
                    PlatformType code = PlatformType.findByCode(platformType);
                    if (code == null) throw new ServiceException("平台类型错误");
                    throw new ServiceException(String.format("账号无[%s]平台权限", code.getName()));
                }
                break;
            }
        }
        return platformType;
    }


    protected void putDataScopeSql(JoinPoint joinPoint, String sqlString, Integer index) {
        if (StringUtils.isNotBlank(sqlString)) {
            if (index != null && index != 0) sqlString = sqlString.substring(4);
            Object param = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(param) && param instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) param;
                Map<String, Object> paramsMap = baseEntity.getParams();
                if (paramsMap.containsKey(DATA_SCOPE)) {
                    StringBuilder sb = new StringBuilder()
                            .append(paramsMap.get(DATA_SCOPE))
                            .append(" AND (" + sqlString + ")");
                    paramsMap.put(DATA_SCOPE, sb.toString());
                    return;
                }
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString + ")");
            }
        }
    }

}
