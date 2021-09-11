package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.UserMapper;
import com.hzc.demo.pojo.User;
import com.hzc.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.hzc.demo.commom.ErrorEnum.*;

/**
 * @author hzc
 * @date 2021/8/2
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Result addUser(User user) {
        if (user == null) return Result.fail(INVALID_PARAMS);
        if ( user.getUserName().isEmpty() || user.getUserPwd().isEmpty() || user.getUserEmail().isEmpty())
            return Result.fail(INPUT_FAIL);
        if (user.getUserRight() < 1 || user.getUserRight()>3) return Result.fail(RIGHT_NOTEXIT);
        if (userMapper.checkName(user.getUserName()) != 0) return Result.fail(USER_NAMEEXIT);
        if (userMapper.checkMobile(user.getUserMobile()) != 0) return Result.fail(USER_MOBILEEXIT);
        userMapper.addUser(user);
        return Result.OK();
    }

    @Override
    public Result deleteUser(int id) {
        if (userMapper.deleteUser(id) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result getUser(int id) {
        User user = userMapper.getUser(id);
        if (user == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(user);
    }

    @Override
    public Result listUser() {
        List<User> userList = userMapper.listUser();
        if ( userList == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(userList);
    }

    @Override
    public Result updateUser(User user) {
        if (user == null) return Result.fail(INVALID_PARAMS);
        if (user.userName.isEmpty() || user.getUserPwd().isEmpty()) return  Result.fail(INPUT_FAIL);
        if (user.getUserRight()< 1 || user.getUserRight() > 3) return Result.fail(RIGHT_NOTEXIT);
        if (userMapper.updateUser(user) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result login(String userName,String userPwd) {
        if (userName.isEmpty() || userPwd.isEmpty()) return Result.fail(INPUT_FAIL);
        User user = userMapper.checkLogin(userName,userPwd);
        if (user == null) return Result.fail(LOGIN_FAIL);
        return Result.OK(user);
    }

}
