package com.hzc.demo.dao;

import com.hzc.demo.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 注册用户
     * @return
     */
    int addUser(@Param("user") User user);

    /**
     * 删除用户
     * @param id 所删除用户的id
     * @return
     */
    int deleteUser(int id);

    /**
     * 修改用户信息
     * @param user 新的用户信息
     * @return 判断是否修改成功
     */
    int updateUser(@Param("user") User user);

    /**
     * 获取某用户信息
     * @param id 所获取的用户id
     * @return 所查用户
     */
    User getUser(int id);

    /**
     * 查询所有用户
     * @return  所有用户集合
     */
    List<User> listUser();
    /**
     * 添加角色权限
     * @param userId 用户id
     * @param rightId   权限id
     * @return
     */
    int addUserRight(@Param("userId") int userId, @Param("userId") int rightId);

    /**
     * 修改用户权限
     * @param userId 用户id
     * @return
     */
    int updateUserRight(@Param("userId") int userId, @Param("rightId") int rightId);

    /**
     * 查询用户权限
     * @param userId 用户id
     * @return  用户权限码
     */
    int getUserRight(int userId);

    /**
     * 验证登录信息是否符合
     * @param userName 用户名
     * @param userPwd 用户密码
     * @return 返回登录账号信息
     */
    User checkLogin(@Param("userName") String userName, @Param("userPwd") String userPwd);

    /**
     * 验证手机号是否唯一
     * @param mobile 所修改或者添加的手机号
     * @return  已存在则返回手机号，不存在则返回空串
     */
    int checkMobile(String mobile);

    /**
     * 验证用户名是否唯一
     * @param name 所修改或者添加的用户名
     * @return  已存在返回用户名，不存在返回空串
     */
    int checkName(String name);
}
