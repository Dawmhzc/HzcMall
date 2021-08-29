package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.UserMapper;
import com.hzc.demo.pojo.User;
import com.hzc.demo.pojo.UserDto;
import com.hzc.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
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
        userMapper.deleteUserRight(id);
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
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> userList = userMapper.listUser();
        for (User user:userList) {
            UserDto userDto = new UserDto();
            int right = userMapper.getUserRight(user.getUserId());
            userDto.setUserId(user.getUserId());
            userDto.setUserName(user.getUserName());
            userDto.setUserPwd(user.getUserPwd());
            userDto.setUserMobile(user.getUserMobile());
            userDto.setUserEmail(user.getUserEmail());
            userDto.setRightId(right);
            userDtoList.add(userDto);
        }
        return Result.OK(userDtoList);
    }

    @Override
    public Result updateUser(UserDto userDto) {
        if (userDto == null || userDto.userName.isEmpty() || userDto.getUserPwd().isEmpty()) return Result.fail(INVALID_PARAMS);
        if (userDto.getRightId()< 1 || userDto.getRightId() > 3) return Result.fail(RIGHT_NOTEXIT);
        if (userMapper.updateUserRight(userDto.getUserId(),userDto.getRightId()) == 0) return Result.fail(DATE_NOEXIT);
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setUserPwd(userDto.getUserPwd());
        user.setUserMobile(userDto.getUserMobile());
        user.setUserEmail(userDto.getUserEmail());
        if (userMapper.updateUser(user) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result login(String userName,String userPwd) {
        if (userName.isEmpty() || userPwd.isEmpty()) return Result.fail(INVALID_PARAMS);
        User user = userMapper.checkLogin(userName,userPwd);
        if (user == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(user);
    }

    @Override
    public Result getUserRight(int userId) {
        int userRight = userMapper.getUserRight(userId);
        if (userRight == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK(userRight);
    }
}
