package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.UserMapper;
import com.hzc.demo.pojo.User;
import com.hzc.demo.pojo.UserDto;
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
    private static int ID_USER = 202100;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result addUser(UserDto userDto) {
        if (userDto == null || userDto.getUserName().isEmpty() || userDto.getUserPwd().isEmpty())
            return Result.fail(INVALID_PARAMS);
        if (userDto.getRightId() < 1 || userDto.getUserId()>3)
            return Result.fail(RIGHT_NOTEXIT);
        if (userMapper.checkName(userDto.getUserName()) != 0) return Result.fail(USER_NAMEEXIT);
        if (userMapper.checkMobile(userDto.getUserMobile()) != 0) return Result.fail(USER_MOBILEEXIT);
        User user = new User();
        user.setUserId(ID_USER);
        user.setUserName(userDto.getUserName());
        user.setUserPwd(userDto.getUserPwd());
        user.setUserMobile(userDto.getUserMobile());
        user.setUserEmail(userDto.getUserEmail());
        userMapper.addUser(user);
        userMapper.addUserRight(ID_USER,userDto.getRightId());
        ID_USER++;
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
        if (userList == null) Result.fail(DATE_NOEXIT);
        return Result.OK(userList);
    }

    @Override
    public Result updateUser(User user) {
        if (user == null || user.userName.isEmpty() || user.getUserPwd().isEmpty()) return Result.fail(INVALID_PARAMS);
        return Result.OK(userMapper.updateUser(user));
    }

    @Override
    public Result login() {
        return Result.OK();
    }

    @Override
    public Result updateUserRight(UserDto userDto) {
        if (userDto == null) return Result.fail(INVALID_PARAMS);
        if (userDto.getUserId()< 1 || userDto.getUserId() > 3) return Result.fail(RIGHT_NOTEXIT);
        if (userMapper.updateUserRight(userDto.getUserId(),userDto.getRightId()) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result getUserRight(int userId) {
        int userRight = userMapper.getUserRight(userId);
        if (userRight == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK(userRight);
    }
}
