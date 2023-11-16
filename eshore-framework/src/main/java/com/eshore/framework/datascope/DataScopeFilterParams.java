package com.eshore.framework.datascope;

import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.enums.DataScopeType;
import lombok.Data;
import org.aspectj.lang.JoinPoint;

/**
 * @author shanglangxin
 * @since 2023/5/12 9:41
 */
@Data
public class DataScopeFilterParams {

    JoinPoint joinPoint;

    SysUser user;

    String deptAlias;

    String userAlias;

    DataScopeType dataScopeType;

    String tableAlias;

    boolean findInSet;

    String column;
}
