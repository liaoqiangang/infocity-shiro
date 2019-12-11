package com.infocity.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @author liaoqiangang
 * @date 2019/12/3 8:59 PM
 * @desc    shiro认证，授权，角色测试
 * @lastModifier
 * @lastModifyTime
 **/
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("lqg","123","admin","user");
    }


    @Test
    public void anthenticationTest(){
        //1、构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm)  ;
        //2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("lqg","123");
        //登录 认证   最后都是在realm中进行认证
        subject.login(token);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
        try {
            //授权  角色
            subject.checkRole("admin");
            subject.checkRoles("admin","user");
        } catch (AuthorizationException e) {
            e.printStackTrace();
            System.out.println("角色");
        }
        //退出
        subject.logout();;
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
    }
}
