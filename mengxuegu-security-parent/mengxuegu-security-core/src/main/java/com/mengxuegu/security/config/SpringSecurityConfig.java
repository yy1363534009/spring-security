package com.mengxuegu.security.config;

import com.mengxuegu.security.authentication.code.ImageCodeValidateFilter;
import com.mengxuegu.security.authentication.mobile.MobileAuthenticationConfig;
import com.mengxuegu.security.authentication.mobile.MobileValidaterFilter;
import com.mengxuegu.security.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * springsecurity核心配置累 主要继承WebSecurityConfigurerAdapter
 *
 * @Auther: yuyue
 * @create 2020/6/14 13:04
 */
@Configuration
@EnableWebSecurity  //开启springsecurity过滤器链
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /*
    用户名密码校验服务
     */
    @Autowired
    private UserDetailsService customUserDetailsServic;

    /*
    springboot的全局配置
     */
    @Autowired
    private SecurityProperties securityProperties;

    /*
    自定义登录成功处理器
     */
    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    /*
    自定义登录失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    /*
    图形验证码校验（校验成功后在校验用户名密码校验）
     */
    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;

    /*
    数据源
     */
    @Autowired
    private DataSource dataSource;

    /*
    手机号校验过滤器
     */
    @Autowired
    private MobileValidaterFilter mobileValidaterFilter;

    /*
    组合手机号认证配置管理器链
     */
    @Autowired
    private MobileAuthenticationConfig mobileAuthenticationConfig;

    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * rememberMe保存用户信息到数据库
     *
     * @return
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);//自动创建rememberMe功能的数据库表，第一次需要创建，第二次需要关闭
        return jdbcTokenRepository;
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
//        //用户信息存储在内存中，密码必须加密，否则报错：There is no PasswordEncoder mapped for the id "null"
//        String password = passwordEncoder().encode("1234");
//        //密码加密形式：（用户输入密码+颜值（随机数））在进行加密
//        logger.info("加密之后存储的密码:" + password);
//        auth.inMemoryAuthentication().withUser("mengxuegu").password(password).authorities("ADMIN");

        //用户信息存储在数据库中
        auth.userDetailsService(customUserDetailsServic);
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
        //http.httpBasic() //httpBasic方式 模态框登录页面，无法自定义登录页面
        http.addFilterBefore(mobileValidaterFilter, UsernamePasswordAuthenticationFilter.class)//在用户名密码校验前加上手机号校验
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)//在用户名密码校验前加上图形验证码校验
                .formLogin() //httpForm方式
                .loginPage(securityProperties.getAuthentication().getLoginPage())//请求页面URL
                .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl())//login.html表单的请求URL
                .usernameParameter(securityProperties.getAuthentication().getUsernameParameter())//默认username，login.html的用户名参数
                .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())//默认password，login.html的密码参数
                .successHandler(customAuthenticationSuccessHandler) //定义自定义成功处理器 实现AuthenticationSuccessHandler接口
                .failureHandler(customAuthenticationFailureHandler) //定义自定义失败处理器 实现AuthenticationFailureHandler接口
                .and()
                .authorizeRequests() // 认证请求
                //放行/login/page,静态资源一同被拦截。否则出现一直重定向，放行验证码请求uri否则无法加载验证码图片
                .antMatchers(securityProperties.getAuthentication().getLoginPage(), "/code/image", "/mobile/page", "/code/mobile").permitAll()
                .anyRequest().authenticated() // 所有进入应用的HTTP请求都要进行认证，授权
                // 下面添加remember-me功能
                .and()
                .rememberMe()//记住我功能
                .tokenRepository(jdbcTokenRepository())//保存用户的登录信息到数据库
                .tokenValiditySeconds(60 * 60 * 24 * 7)//记住我的有效时长（一周）
        ;
        http.apply(mobileAuthenticationConfig);
    }

    /**
     * 针对静态资源进行放行
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
    }

}
