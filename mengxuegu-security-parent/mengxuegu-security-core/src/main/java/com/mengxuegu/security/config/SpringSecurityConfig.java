package com.mengxuegu.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Auther: yuyue
 * @create 2020/6/14 13:04
 */
@Configuration
@EnableWebSecurity  //开启springsecurity过滤器链
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证管理器:
     * 1.认证信息提供方式(用户名、密码、当前用户的资源权限)
     * 2.可采用内存存储方式，也可能采用数据库方式等
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //用户信息存储在内存中，密码必须加密，否则报错：There is no PasswordEncoder mapped for the id "null"
        String password = passwordEncoder().encode("1234");
        //密码加密形式：（用户输入密码+颜值（随机数））在进行加密
        logger.info("加密之后存储的密码:" + password);
        auth.inMemoryAuthentication().withUser("mengxuegu").password(password).authorities("ADMIN");
    }

    /**
     * 资源权限配置（过滤器链）:
     * 1.拦截的哪一些资源
     * 2.资源所对应的角色权限
     * 3.定义认证方式:httpBasic 、httpForm
     * 4.定制登录页面、登录请求地址、错误处理方式
     * 5.自定义 spring security 过滤器等
     * 注：当你认认证成功后，springsecurity会重定向到你上一次的请求上（验证码）
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic() //httpBasic方式
        http.formLogin() //httpForm方式
                .loginPage("/login/page")
                .loginProcessingUrl("/login/form")//login.html的请求
                .usernameParameter("name")//默认username，login.html的用户名参数
                .passwordParameter("pwd")//默认password，login.html的密码参数
                .and()
                .authorizeRequests() // 认证请求
                .antMatchers("/login/page").permitAll() //放行/login/page,静态资源一同被拦截。否则出现一直重定向
                .anyRequest().authenticated() // 所有进入应用的HTTP请求都要进行认证，授权
        ;
    }

    /**
     * 针对静态资源进行放行
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/dist/**","/modules/**","/plugins/**");
    }

    

}
