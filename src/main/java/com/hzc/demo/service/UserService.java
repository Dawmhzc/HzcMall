package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.User;


public interface UserService {
    /**
     * 用户注册
     * @param user  手机号码只能绑定唯一账号
     * @return
     */
    Result addUser(User user);

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
    Result login(String userName,String userPwd);

}
