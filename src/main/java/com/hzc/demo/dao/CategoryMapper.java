package com.hzc.demo.dao;

import com.hzc.demo.pojo.GoodsCategory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryMapper {

    @Delete("delete from goods_category where category_id=#{id}")
    int deleteById(Integer id);

    @Insert("insert into goods_category (category_level, parent_id, category_name) values (#{categoryLevel},#{parentId},#{categoryName})")
    int insert(GoodsCategory category);

    @Select("select * from goods_category where category_id=#{id}")
    GoodsCategory selectById(Integer id);

    @Select("select * from goods_category where category_level=#{level} and category_name=#{name}")
    GoodsCategory selectByLevelAndName(@Param("level") Integer categoryLevel, @Param("name") String categoryName);

    @Update("<script> update goods_category\n" +
            "            <set>\n" +
            "                <if test=\"categoryLevel!=null and categoryLevel!=''\">\n" +
            "                    category_level=#{categoryLevel},\n" +
            "                </if>\n" +
            "                <if test=\"parentId!=null and parentId!=''\">\n" +
            "                    and parent_id=#{parentId},\n" +
            "                </if>\n" +
            "                <if test=\"categoryName!=null and categoryName!=''\">\n" +
            "                    and category_name=#{categoryName}\n" +
            "                </if>\n" +
            "            </set>\n" +
            "            where category_id=#{categoryId} " +
            "</script>")
    int updateById(Map<String,Object> map);

    @Select("select * from goods_category where parent_id=#{parentId}")
    List<GoodsCategory> selectByParentId(Integer parentId);

    @Select("select * from goods_category where category_level=#{level}")
    List<GoodsCategory> selectByLevel(Integer level);

}
