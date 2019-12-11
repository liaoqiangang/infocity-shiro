package com.infocity.controller;

import com.infocity.dto.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liaoqiangang
 * @date 2019/12/11 10:03 AM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@Controller
@RequestMapping("/")
public class LoginController {

    @RequestMapping("403")
    public String to403(ModelMap map) {
        // 设置属性
        map.addAttribute("username", "liuhuan");
        return "403";
    }
    @RequestMapping("toLogin")
    public String testThymeleaf(ModelMap map) {
        // 设置属性
        map.addAttribute("username", "liuhuan");
        return "login";
    }

    @RequestMapping(value = "sublogin", method = RequestMethod.POST)
    @ResponseBody
    public String sublogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            token.setRememberMe(user.isRememberme());
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if (subject.hasRole("管理员")) {
            return "有管理员的权限";
        }
        return "登录成功,无管理员权限";
    }


//    @RequiresPermissions("user:select")
    @RequestMapping(value = "userSelect", method = RequestMethod.GET)
    @ResponseBody
    public String userSelect() {
        return "有user:select权限";
    }

//    @RequiresPermissions("user:list")
    @RequestMapping(value = "userList", method = RequestMethod.GET)
    @ResponseBody
    public String userList() {
        return "有user:list权限";
    }


//    @RequiresRoles("作家")
    @RequestMapping(value = "testRole", method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "有作家的角色";
    }

    //注释掉，通过shiro过滤器进行角色验证
//    @RequiresRoles("管理员")
    @RequestMapping(value = "testRole1", method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "有user:list权限";
    }

}
