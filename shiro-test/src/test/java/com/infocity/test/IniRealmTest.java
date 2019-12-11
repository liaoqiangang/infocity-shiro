package com.infocity.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author liaoqiangang
 * @date 2019/12/3 9:12 PM
 * @desc    inirealm测试
 * @lastModifier
 * @lastModifyTime
 **/
public class IniRealmTest {


    @Test
    public void anthenticationTest() {
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        //1、构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        //2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("lqg", "123");
        //登录   最后都是在realm中进行认证
        subject.login(token);
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        try {
            subject.checkRole("admin");
//            subject.checkRoles("admin","user");
        } catch (AuthorizationException e) {
            e.printStackTrace();
            System.out.println("未授权admin角色");
        }
        ////授权  权限信息验证 用户删除，用户修改权限
        subject.checkPermission("user:update");
        subject.checkPermissions("user:delete", "user:update");
        //退出
        subject.logout();
        ;
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
    }
}
