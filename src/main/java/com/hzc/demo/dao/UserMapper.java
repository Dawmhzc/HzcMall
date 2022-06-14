package com.hzc.demo.dao;

import com.hzc.demo.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    @Insert("insert into user(user_name, user_pwd, user_mobile, user_email, user_right) values (#{userName},#{userPwd},#{userMobile},#{userEmail},#{userRight})")
    int addUser(User user);

    @Delete("delete from mall.user where user_id = #{id}")
    int deleteUser(int id);

    @Update("update mall.user set user_name=#{userName}, user_pwd=#{userPwd}, user_mobile=#{userMobile}, user_email=#{userEmail}, user_right=#{userRight} where user_id=#{userId}")
    int updateUser(User user);

    @Select("select * from mall.user where user_id=#{id}")
    User getUser(int id);

    @Select("select * from mall.user")
    List<User> listUser();

    @Select("select * from mall.user where user_name=#{userName} and user_pwd=#{userPwd}")
    User checkLogin(@Param("userName") String userName, @Param("userPwd") String userPwd);

    @Select("select count(1) from mall.user where user_mobile=#{mobile}")
    int checkMobile(String mobile);

    @Select("select count(1) from mall.user where user_name=#{name}")
    int checkName(String name);

}
