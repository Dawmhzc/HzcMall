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
    @Insert("Insert into order(ord_id, ord_user_id, ord_address, ord_buytime, ord_total, ord_status, ord_good_id, ord_good_numb, ord_shop_id) valuse (#{ordId},#{ordUserId},#{ordAddress},#{ordBuyTime},#{ordTotal},#{ordStatus},#{ordGoodId},#{ordGoodNumb},#{ordShopId})")
    int addOrder(Order order);

    @Delete("delete from order where ord_id=#{ordId}")
    int deleteOrder(String ordId);

    @Update("update order set ord_user_id=#{ordUserId}, ord_address=#{ordAddress}, ord_buytime=#{ordBuyTime}, ord_total=#{ordTotal}, ord_status=#{ordStatus}, ord_good_id=#{ordGoodId}, ord_good_numb=#{ordGoodNumb}, ord_shop_id=#{ordShopId} where ord_id=#{ordId}")
    int updateOrder(Order order);

    @Select("select * from order where ord_id=#{ordId}")
    Order getOrder(String ordId);

    @Select("select * from order where ord_user_id=#{userId}")
    List<Order> listUserOrder(int  userId);

    @Select("select * from order where ord_shop_id=#{shopId}")
    List<Order> listShopOrder(int shopId);

    @Select("select * from order")
    List<Order> listAllOrder();
}
