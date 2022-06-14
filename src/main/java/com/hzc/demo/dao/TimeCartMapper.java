package com.hzc.demo.dao;

import com.hzc.demo.pojo.TimeCart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeCartMapper {
    @Select("select * from mall.time_cart where user_id=#{id}")
    List<TimeCart> listTimeCart(int id);
    @Select("select * from mall.time_cart where id=#{id}")
    TimeCart getTimeCart(int id);
    @Delete("delete from mall.time_cart where id = #{id}")
    int delTimeCart(int id);
    @Update("update mall.time_cart set user_id=#{userId}, goods_id=#{goodsId}, goods_numb=#{goodsNumb}, address=#{address}, mobile=#{mobile}, go_time=#{goTime}, state=#{state} where id=#{id}")
    int updTimeCart(TimeCart timeCart);
    @Insert("insert into mall.time_cart(user_id, goods_id, goods_numb, address, mobile, go_time, state) values (#{userId}, #{goodsId}, #{goodsNumb}, #{address}, #{mobile}, #{goTime}, #{state})")
    int addTimeCart(TimeCart timeCart);
    @Select("select * from mall.time_cart where go_time=#{goTime} and state=#{state}")
    List<TimeCart> weakOrMouth(@Param("goTime") String goTime, @Param("state") int state);
}
