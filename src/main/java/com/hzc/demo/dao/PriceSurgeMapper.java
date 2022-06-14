package com.hzc.demo.dao;

import com.hzc.demo.pojo.PriceSurge;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceSurgeMapper {

    @Select("select * from price_surge where goods_id=#{goodsId}")
    PriceSurge selectSurge(Integer goodsId);
    @Insert("insert into price_surge(surge,goods_id) value(#{surge},#{goodsId})")
    int insertSurge(Integer goodsId, String surge);
    @Update("update price_surge set surge=#{surge} where goods_id=#{goodsId}")
    int updateSurge(String surge, Integer goodsId);
    @Delete("delete from price_surge where goods_id=#{goodsId}")
    int deleteSurge(Integer goodsId);
}
