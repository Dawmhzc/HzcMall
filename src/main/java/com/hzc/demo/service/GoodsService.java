package com.hzc.demo.service;

import com.hzc.demo.pojo.Goods;
import com.hzc.demo.util.CategoryMap;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    Goods getGoodsById(Integer goodsId);
    List<Goods> getGoodsList(Map<String,Object> map);
    int saveGoods(Goods goods);
    int saveGoodsList(List<Goods> goodsList);
    int editGoods(Map<String,Object> map);
    int downGoods(List<Integer> goodsId_list, Integer sellStatus);
    int delGoods(Integer goodsId);
    int delGoodsList(List<Integer> goodsId_list);
    List<Goods> searchGoodsByKeyword(Map<String,Object> map);
    List<Goods> findGoodsByLevel(CategoryMap categoryMap, Integer sellStatus);
}
