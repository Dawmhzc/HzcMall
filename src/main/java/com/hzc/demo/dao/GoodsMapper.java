package com.hzc.demo.dao;

import com.hzc.demo.pojo.Goods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsMapper {

    @Delete("delete from goods where goods_id=#{id}")
    int deleteById(Integer id);

    @Insert("insert into goods(goods_name, goods_intro, goods_category_id, goods_img, original_price, selling_price,\n" +
            "                          sell_status,stock_num, create_user, create_time)\n" +
            "        values (#{goodsName},#{goodsIntro},#{goodsCategoryId},\n" +
            "                #{goodsImg},#{originalPrice},#{sellingPrice},\n" +
            "                #{sellStatus},#{stockNum},#{createUser},#{createTime})")
    int insert(Goods goods);

    @Insert("<script>" +
            "insert into goods(goods_name, goods_intro, goods_category_id, goods_img, original_price, selling_price,\n" +
            "                          sell_status,stock_num, create_user, create_time)\n" +
            "        values\n" +
            "        <foreach collection=\"goodsList\" item=\"goods\" separator=\",\">\n" +
            "            (#{goodsName},#{goodsIntro},#{goodsCategoryId},\n" +
            "            #{goodsImg},#{originalPrice},#{sellingPrice},\n" +
            "            #{sellStatus},#{stockNum},#{createUser},#{createTime})\n" +
            "        </foreach>"+
            "</script>")
    int batchInsert(List<Goods> goodsList);

    @Update("update goods set sell_status=#{sellStatus} where goods_id=#{goodsId}")
    int updateSellStatus(Map<String,Object> map);
    //int batchUpdateNum(List<Integer> nums);

    @Update("<script> update goods\n" +
            "            <set>\n" +
            "                <if test=\"goodsName!=null and goodsName!=''\">\n" +
            "                    goods_name=#{goodsName},\n" +
            "                </if>\n" +
            "                <if test=\"goodsIntro!=null and goodsIntro!=''\">\n" +
            "                    goods_intro=#{goodsIntro},\n" +
            "                </if>\n" +
            "                <if test=\"goodsCategoryId!=null and goodsCategoryId!=''\">\n" +
            "                    goods_category_id=#{goodsCategoryId},\n" +
            "                </if>\n" +
            "                <if test=\"goodsImg!=null and goodsImg!=''\">\n" +
            "                    goods_img=#{goodsImg},\n" +
            "                </if>\n" +
            "                <if test=\"originalPrice!=null and originalPrice!=''\">\n" +
            "                    original_price=#{originalPrice},\n" +
            "                </if>\n" +
            "                <if test=\"sellingPrice!=null and sellingPrice!=''\">\n" +
            "                    selling_price=#{sellingPrice},\n" +
            "                </if>\n" +
            "                <if test=\"stockNum!=null and stockNum!=''\">\n" +
            "                    stock_num=#{stockNum},\n" +
            "                </if>\n" +
            "                <if test=\"sellStatus!=null and sellStatus!=''\">\n" +
            "                    sell_status=#{sellStatus},\n" +
            "                </if>\n" +
            "                <if test=\"createUser!=null and createUser!=''\">\n" +
            "                    create_user=#{createUser}\n" +
            "                </if>\n" +
            "            </set>\n" +
            "        where goods_id=#{goodsId}</script>")
    int updateById(Map<String,Object> map);

    //int batchUpdateSellStatus(List<Integer> ids);

    @Select("select * from goods where goods_id=#{id}")
    Goods selectById(Integer id);

    @Select("select * from goods where goods_name=#{goodsName} and goods_category_id=#{goodsCategoryId} and create_user=#{createUser}")
    Goods selectByCategoryIdAndNameAndUser(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Integer goodsCategoryId, @Param("createUser") Integer createUser);

    @Select("select * from goods where goods_id in (${ids})")
    List<Goods> selectGoodsListByIds(String ids);

    @Select("<script> select * from goods <where>" +
            "<if test=\"goodsName!=null and goodsName!=''\"> and goods_name like concat('%',#{goodsName},'%') </if>" +
            "<if test=\"goodsCategoryId!=null and goodsCategoryId!=''\"> and goods_category_id = #{goodsCategoryId}" +
            "<if test=\"sellStatus!=null and sellStatus!=''\"> and sell_status=#{sellStatus} </if>"+
            "</if> </where> </script>")
    List<Goods> selectGoodsBySearch(Map<String,Object> map);

    @Select("<script> select * from goods <where>" +
            "<if test=\"keyword!=null and keyword!=''\">" +
            "   and (goods_name like concat('%',#{keyword},'%') or goods_intro like concat('%',#{keyword},'%')) </if>" +
            "<if test=\"goodsCategoryId!=null and goodsCategoryId!=''\"> and goods_category_id = #{goodsCategoryId} </if>" +
            "<if test=\"sellStatus!=null and sellStatus!=''\"> and sell_status=#{sellStatus} </if>" +
            "</where> </script>")
    List<Goods> selectGoodsByKeyword(Map<String,Object> map);

    @Select("select count(goods_category_id) from goods where goods_category_id=#{goodsCategoryId}")
    int selectGoodsByCategoryId(Integer goodsCategoryId);
}
