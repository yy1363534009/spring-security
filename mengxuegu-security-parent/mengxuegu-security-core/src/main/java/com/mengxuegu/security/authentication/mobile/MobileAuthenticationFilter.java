package com.mengxuegu.security.authentication.mobile;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: yuyue
 * @create 2020/6/28 14:43
 */
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /*
    用户登录页面输出手机号的参数
     */
    private String mobileParameter = "mobile";

    /*
    是否为post请求
     */
    private boolean postOnly = true;

    public MobileAuthenticationFilter() {
        // 登录表单请求url
        super(new AntPathRequestMatcher("/mobile/form", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainUsername(request);

        if (mobile == null) {
            mobile = "";
        }

//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
//                username, password);

        MobileAuthenticationToken authRequest = new MobileAuthenticationToken(mobile);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取请求中用户输入的手机号
     */
    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    /**
     * 将请求中的sessionID和host主机ip放到MobileAuthenticationToken中
     */
    protected void setDetails(HttpServletRequest request,
                              MobileAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getMobileParameter() {
        return mobileParameter;
    }

    public void setMobileParameter(String mobileParameter) {
        this.mobileParameter = mobileParameter;
    }

}

