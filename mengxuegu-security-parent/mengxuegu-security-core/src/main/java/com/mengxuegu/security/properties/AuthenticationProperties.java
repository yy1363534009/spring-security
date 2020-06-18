package com.mengxuegu.security.properties;

/**
 * @Auther: yuyue
 * @create 2020/6/16 10:11
 */
public class AuthenticationProperties {

    private String loginPage = "/login/page";

    private String loginProcessingUrl = "/login/form";

    private String usernameParameter = "name";

    private String passwordParameter = "pwd";

    private String[] staticPaths = {"/dist/**", "/modules/**", "/plugins/**"};

    private LoginResponseType loginType= LoginResponseType.REDIRECT;

    public LoginResponseType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginResponseType loginType) {
        this.loginType = loginType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getLoginProcessingUrl() {
        return loginProcessingUrl;
    }

    public void setLoginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
    }

    public String getUsernameParameter() {
        return usernameParameter;
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    public String getPasswordParameter() {
        return passwordParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    public String[] getStaticPaths() {
        return staticPaths;
    }

    public void setStaticPaths(String[] staticPaths) {
        this.staticPaths = staticPaths;
    }

}
