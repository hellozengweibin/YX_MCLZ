package com.eshore.framework.datascope;

/**
 * @author shanglangxin
 * @since 2023/5/12 9:38
 */
public interface IDataScopeFilter {

    /**
     * 数据权限过滤
     *
     * @param params
     */
    void doFilter(DataScopeFilterParams params);

}
