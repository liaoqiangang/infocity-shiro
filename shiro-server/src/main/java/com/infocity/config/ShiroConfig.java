package com.infocity.config;

import com.infocity.cache.RedisCacheManager;
import com.infocity.filter.RolesOrFilter;
import com.infocity.realm.CustomRealm;
import com.infocity.session.CustomSessionManager;
import com.infocity.session.RedisSessionDao;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author liaoqiangang
 * @date 2019/12/11 9:32 AM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@Configuration
public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/toLogin");
        shiroFilter.setUnauthorizedUrl("403");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/sublogin", "anon");
        //具有作家的角色，才可以访问
        filterChainDefinitionMap.put("/testRole", "roles[\"作家\"]");
        //同时具有作家，管理员的角色才可以访问
        filterChainDefinitionMap.put("/testRole1", "rolesOr[\"作家\",\"管理员\"]");

        filterChainDefinitionMap.put("/userSelect", "perms[\"user:select\"]");
        filterChainDefinitionMap.put("/userList", "perms[\"user:list\",\"user:add\",\"user:update\"]");
        filterChainDefinitionMap.put("/*", "authc");


        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //添加自定义的拦截器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("rolesOr", rolesOrFilter());
        shiroFilter.setFilters(filterMap);
        return shiroFilter;
    }

    /**
     * 创建 SecurityManager 对象
     * 设置session管理器
     * 缓存管理器   都存储到redis中
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(customRealm());
        //设置session redis
        securityManager.setSessionManager(sessionManager());
        //缓存 权限，信息
        securityManager.setCacheManager(redisCacheManager());
        //记住我
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }


    /**
     * 创建customrealm，设置加密器
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    /**
     * 创建加密器，设置加密算法，和次数
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public RolesOrFilter rolesOrFilter() {
        RolesOrFilter rolesOrFilter = new RolesOrFilter();
        return rolesOrFilter;
    }

    /**
     * 该成自定义的CustomSessionManager，避免频繁读取redis
     *
     * @return
     */
    @Bean
    public CustomSessionManager sessionManager() {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDao());
        return sessionManager;
    }

    @Bean
    public RedisSessionDao redisSessionDao() {
        return new RedisSessionDao();
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        return redisCacheManager;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie());
        return cookieRememberMeManager;
    }

    @Bean
    public SimpleCookie cookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberme");
        //7天
        simpleCookie.setMaxAge(60 * 60 * 24 * 7);
        return simpleCookie;
    }

}
