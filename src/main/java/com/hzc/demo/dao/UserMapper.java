package com.hzc.demo.dao;

import com.hzc.demo.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    @Insert("insert into user(user_id, user_name, user_pwd, user_mobile, user_email) values (#{userId},#{userName},#{userPwd},#{userMobile},#{userEmail})")
    int addUser(User user);

    @Delete("delete from user where user_id = #{id}")
    int deleteUser(int id);

    @Update("update user set user_name=#{userName}, user_pwd=#{userPwd}, user_mobile=#{userMobile}, user_email=#{userEmail} where user_id=#{userId}")
    int updateUser(User user);

    @Select("select * from user where user_id=#{id}")
    User getUser(int id);

    @Select("select * from user")
    List<User> listUser();

    @Insert("insert into user_right(user_id, right_id) values (#{userId}, #{rightId})")
    int addUserRight(@Param("userId") int userId, @Param("rightId") int rightId);

    @Update("update user_right set right_id=#{rightId} where user_id=#{userId}")
    int updateUserRight(@Param("userId") int userId, @Param("rightId") int rightId);

    @Select("select right_id from user_right where user_id=#{userId}")
    int getUserRight(int userId);

    @Select("select * from user where user_name=#{userName} and user_pwd=#{userPwd}")
    User checkLogin(@Param("userName") String userName, @Param("userPwd") String userPwd);

    @Select("select count(1) from user where user_mobile=#{mobile}")
    int checkMobile(String mobile);

    @Select("select count(1) from user where user_name=#{name}")
    int checkName(String name);
}
