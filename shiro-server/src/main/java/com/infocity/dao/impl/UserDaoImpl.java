package com.infocity.dao.impl;

import com.infocity.dao.UserDao;
import com.infocity.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author liaoqiangang
 * @date 2019/12/11 10:52 AM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询用户密码
     * @param username
     * @return
     */
    @Override
    public User selectPasswordByUsername(String username) {
        String sql = "SELECT username,`password` FROM sys_user WHERE username = ?";
        List<User> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询用户角色
     * @param username
     * @return
     */
    @Override
    public List<String> queryRolesByUsername(String username) {
        String sql = "SELECT role_name FROM sys_role r JOIN sys_user u ON u.role_id = r.id WHERE u.username = ?";
        return jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
    }

    /**
     * 查询用户权限
     * @param username
     * @return
     */
    @Override
    public List<String> queryPermissionsByUserName(String username) {
        String sql = "SELECT permission_code FROM sys_permission p JOIN sys_role_permission rp ON p.id = rp.permission_id JOIN sys_role r ON rp.role_id = r.id JOIN sys_user u ON r.id = u.role_id WHERE u.username = ?";
        return jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("permission_code");
            }
        });
    }
}
