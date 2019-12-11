package com.infocity.dao;

import com.infocity.dto.User;

import java.util.List;
import java.util.Set;

/**
 * @author liaoqiangang
 * @date 2019/12/11 10:52 AM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
public interface UserDao {
    User selectPasswordByUsername(String username);

    List<String> queryRolesByUsername(String username);

    List<String> queryPermissionsByUserName(String username);
}
