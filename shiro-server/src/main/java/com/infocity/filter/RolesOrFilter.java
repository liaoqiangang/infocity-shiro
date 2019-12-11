package com.infocity.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author liaoqiangang
 * @date 2019/12/11 2:26 PM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
public class RolesOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0) {
            return true;
        }
        //只要具备一个角色就返回成功
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}
