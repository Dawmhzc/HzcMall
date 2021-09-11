package com.hzc.demo.dao;

import com.hzc.demo.pojo.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    @Insert("insert into mall.order(ord_id, ord_user_id, ord_address, ord_mobile, ord_buytime, ord_total, ord_good_name, ord_good_numb, ord_shop_id) values (#{ordId},#{ordUserId},#{ordAddress},#{ordMobile},#{ordBuyTime},#{ordTotal},#{ordGoodName},#{ordGoodNumb},#{ordShopId})")
    int addOrder(Order order);

    @Delete("delete from mall.order where ord_id=#{ordId}")
    int deleteOrder(String ordId);

    @Update("update mall.order set ord_user_id=#{ordUserId}, ord_address=#{ordAddress}, ord_mobile=#{ordMobile}, ord_buytime=#{ordBuyTime}, ord_total=#{ordTotal}, ord_good_name=#{ordGoodName}, ord_good_numb=#{ordGoodNumb}, ord_shop_id=#{ordShopId} where ord_id=#{ordId}")
    int updateOrder(Order order);

    @Select("select * from mall.order where ord_id=#{ordId}")
    Order getOrder(String ordId);

    @Select("select * from mall.order where ord_user_id=#{userId} order by ord_buytime desc")
    List<Order> listUserOrder(int userId);


    @Select("select * from mall.order where ord_shop_id=#{shopId} order by ord_buytime desc")
    List<Order> listShopOrder(int shopId);

    @Select("select * from mall.order order by ord_buytime desc")
    List<Order> listAllOrder();
}
