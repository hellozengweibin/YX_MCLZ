package com.eshore.encrypt.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DecryptHttpRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 还是使用Object比较好，防止出现复杂的数据类型
     */
    private Map<String, String[]> parameterMap = new ConcurrentHashMap<>();

    public DecryptHttpRequestWrapper(HttpServletRequest request, Map<String, String> paraMap) {
        super(request);
        this.parameterMap.putAll(request.getParameterMap());
        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            this.parameterMap.put(entry.getKey(), new String[] {entry.getValue()});
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<String>(parameterMap.keySet());
        return vector.elements();
    }

    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        if (results != null && results.length > 0) {
            return results[0];
        } else {
            return null;
        }
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }
}