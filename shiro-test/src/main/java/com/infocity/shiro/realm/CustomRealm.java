package com.infocity.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.Md5CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liaoqiangang
 * @date 2019/12/3 9:44 PM
 * @desc 自定义realm
 * @lastModifier
 * @lastModifyTime
 **/
public class CustomRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    Map<String, String> userMap = new HashMap<>(16);

    {
        //md5加密账户
        userMap.put("liaoqiangang", "e10adc3949ba59abbe56e057f20f883e");
        //md5加密，再加盐之后账户
        userMap.put("liuhuan", "335c538b57608e74e43fddf3acaa4360");
        super.setName("customRealm");
    }

    /**
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByUserName(username);
        Set<String> permissions = getPermissionsByUserName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 从数据库中获取权限数据
     *
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUserName(String username) {
        Set<String> permissions = new HashSet<>();
        permissions.add("user:delete");
        permissions.add("user:add");
        return permissions;
    }

    /**
     * 从数据库中，或者缓存中获取数据
     *
     * @param username
     * @return
     */
    private Set<String> getRolesByUserName(String username) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        return roles;
    }

    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1、从主体传过来的认证信息中，获得用户名
        String username = (String) authenticationToken.getPrincipal();
        //2、通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        //加盐设置近认证器
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("liaoqiangang"));

        return authenticationInfo;
    }

    /**
     * 模拟数据库 查询凭证
     *
     * @param username
     * @return
     */
    private String getPasswordByUserName(String username) {
        return userMap.get(username);
    }


    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456");
        Md5Hash md5Hash_salt = new Md5Hash("123456", "liaoqiangang");
        System.out.println("加密之后，密码为：" + md5Hash.toString());
        System.out.println("加密,再加盐之后，密码为：" + md5Hash_salt.toString());
    }
}
