package com.infocity.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author liaoqiangang
 * @date 2019/12/3 9:20 PM
 * @desc    jdbcrealm 测试
 * @lastModifier
 * @lastModifyTime
 **/
public class JdbcRealmTest {
    DruidDataSource dataSource = new DruidDataSource();

    {
        //默认表结构，可以走默认查询sql
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
        //使用自定义sql语句
//        dataSource.setUrl("jdbc:mysql://localhost:3306/example");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }

    @Test
    public void anthenticationTest() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        //权限验证默认是关闭，需要手动开启
        jdbcRealm.setPermissionsLookupEnabled(true);

        //验证sql
        String authenticationQuery = "select password from test_users where username = ?";
        jdbcRealm.setAuthenticationQuery(authenticationQuery);
        //角色授权查询
        String uerRolesQuery = "select role_name from test_user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(uerRolesQuery);
        //功能授权
        String permissionsQuery = "select permission from test_roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionsQuery);


        //1、构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        //2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("liuhuan", "123");
//        UsernamePasswordToken token = new UsernamePasswordToken("lqg","123");
        //登录   最后都是在realm中进行认证
        subject.login(token);
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        try {
            subject.checkRole("user");
//            subject.checkRoles("admin","user");
        } catch (AuthorizationException e) {
            e.printStackTrace();
            System.out.println("未授权admin角色");
        }

        subject.checkPermission("user:delete");
    }
}
