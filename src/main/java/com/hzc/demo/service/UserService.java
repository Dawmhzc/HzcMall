package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.User;
import com.hzc.demo.pojo.UserDto;


public interface UserService {
    /**
     * 用户注册
     * @param userDto  手机号码只能绑定唯一账号
     * @return
     */
    Result addUser(UserDto userDto);

    /**
     * 管理员账号删除账号
     * @param id
     * @return
     */
    Result deleteUser(int id);

    /**
     * 管理员查询用户
     * @param id
     * @return
     */
    Result getUser(int id);

    /**
     * 管理员查询所有用户
     * @return
     */
    Result listUser();

    /**
     * 用户修改账户信息
     * @param user
     * @return
     */
    Result updateUser(User user);

    /**
     * 用户登录
     * @return
     */
    Result login();

    /**
     * 更改用户权限
     * @param userDto 用户id
     * @return
     */
    Result updateUserRight(UserDto userDto);

    /**
     * 查询用户权限
     * @param userId 用户id
     * @return
     */
    Result getUserRight(int userId);
}
