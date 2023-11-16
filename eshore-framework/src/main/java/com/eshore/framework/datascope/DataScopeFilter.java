package com.eshore.framework.datascope;


import com.eshore.common.enums.DataScopeType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jianlin.liu
 * @since 2023/6/30 9:54
 */
public class DataScopeFilter implements IDataScopeFilter {

    private DataScopeFilter() {

    }

    private static final Map<String, IDataScopeFilter> PROCESSOR_HOLDER = new HashMap<String, IDataScopeFilter>() {{
        for (DataScopeType value : DataScopeType.values()) {
            Object dataScopeFilterInstance = value.getDataScopeFilterInstance();
            if (dataScopeFilterInstance != null && dataScopeFilterInstance instanceof IDataScopeFilter) {
                put(value.getName(), (IDataScopeFilter) dataScopeFilterInstance);
            }
        }
    }};

    private final static DataScopeFilter INSTANCE = new DataScopeFilter();

    public static DataScopeFilter getInstance() {
        return INSTANCE;
    }

    @Override
    public void doFilter(DataScopeFilterParams params) {
        PROCESSOR_HOLDER.get(params.getDataScopeType().getName()).doFilter(params);
    }
}
