package com.eshore.framework.provider;

import com.eshore.framework.auth.PhoneAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @ClassName PhoneAuthenticationProvider
 * @Description 短信登陆鉴权 Provider，要求实现 AuthenticationProvider 接口
 * @Author jianlin.liu
 * @Date 2022/9/15
 * @Version 1.0
 **/
public class PhoneAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhoneAuthenticationToken authenticationToken = (PhoneAuthenticationToken) authentication;

        String telephone = (String) authenticationToken.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(telephone);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        PhoneAuthenticationToken authenticationResult = new PhoneAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return PhoneAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
