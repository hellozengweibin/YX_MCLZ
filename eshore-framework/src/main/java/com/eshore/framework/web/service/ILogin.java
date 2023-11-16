package com.eshore.framework.web.service;

import com.eshore.common.core.domain.model.LoginBody;
import com.eshore.common.core.domain.model.LoginResponseBody;

/**
 * @InterfaceName ILogin
 * @Description 登录接口
 * @Author jianlin.liu
 * @Date 2022/9/15
 * @Version 1.0
 **/
public interface ILogin {

    /**
     * 系统登录接口
     *
     * @param loginBody
     * @return
     */
    LoginResponseBody login(LoginBody loginBody);
}
