package com.hzc.demo.dao;

import com.hzc.demo.pojo.UserAddress;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressMapper {
    @Insert("insert into mall.user_address(user_id, address, mobile) values (#{userId}, #{address}, #{mobile})")
    int addAddress(UserAddress userAddress);

    @Delete("delete from mall.user_address where id = #{id}")
    int deleteAddress(int id);

    @Select("select * from mall.user_address where user_id = #{userId}")
    List<UserAddress> listAddress(int userId);

    @Select("select * from mall.user_address where id = #{id}")
    UserAddress getAddress(int id);
}
