package com.infocity.test;

import com.infocity.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author liaoqiangang
 * @date 2019/12/3 9:55 PM
 * @desc    自定义realm的认证、授权、角色的测试
 * @lastModifier
 * @lastModifyTime
 **/
public class CustomRealmTest {


    @Test
    public void anthenticationTest() {

        CustomRealm customRealm = new CustomRealm();

        //1、构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //加密处理
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //加密算法
        matcher.setHashAlgorithmName("md5");
        //加密次数
        matcher.setHashIterations(1);

        //将加密器 加入到自定义到customrealm中
        customRealm.setCredentialsMatcher(matcher);

        //2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //加密账户认证
//        UsernamePasswordToken token = new UsernamePasswordToken("liaoqiangang", "123456");
        //加密 加盐账户认证
        UsernamePasswordToken token = new UsernamePasswordToken("liuhuan","123456");
        //登录   最后都是在realm中进行认证
        subject.login(token);
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        try {
            subject.checkRole("user");
            subject.checkRoles("admin","user");
        } catch (AuthorizationException e) {
            e.printStackTrace();
            System.out.println("未授权admin角色");
        }
        subject.checkPermission("user:delete");
    }
}
