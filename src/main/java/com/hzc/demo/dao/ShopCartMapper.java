package com.hzc.demo.dao;

import com.hzc.demo.pojo.ShopCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShopCartMapper {

    @Insert("insert into shop_cart(user_id,goods_id,goods_count,create_time) value(#{userId},#{goodsId},#{goodsCount},#{createTime})")
    int saveShopCart(ShopCart shopCart);
    @Update("<script> update shop_cart <set> <if test=\"userId!=null and userId!=''\"> user_id=#{userId} </if> <if test=\"goodsId!=null and goodsId!=''\"> goods_id=#{goodsId} </if> <if test=\"goodsCount!=null and goodsCount!=''\"> goods_count=#{goodsCount} </if> <if test=\"createTime!=null and createTime!=''\"> create_time=#{createTime} </if> </set> where shop_cart_id=#{shopCartId} </script>")
    int updateShopCart(Map<String,Object> map);
    @Delete("delete from shop_cart where shop_cart_id=#{shopCartId}")
    int delShopCart(Integer shopCartId);
    @Select("select * from shop_cart where shop_cart_id=#{shopCartId}")
    ShopCart selectShopCartById(Integer shopCartId);
    @Select("select * from shop_cart where user_id=#{userId}")
    List<ShopCart> selectShopCartList(int userId);
    @Select("select count(goodsId) from shop_cart where goodsId=#{goodsId}")
    int exitShopCart(Integer goodsId);
}
