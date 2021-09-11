package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.IdCollection;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    Goods getGoodsById(Integer goodsId);
    List<Goods> getGoodsList(Map<String,Object> map);
    Result saveGoods(Goods goods);
    Result saveGoodsList(List<Goods> goodsList);
    Result editGoods(Goods goods);
    Result downGoods(List<Integer> goodsId_list, Integer sellStatus);
    Result delGoods(Integer goodsId);
    Result delGoodsList(List<Integer> goodsId_list);
    List<Goods> searchGoodsByKeyword(Map<String,Object> map);
    List<Goods> findGoodsByLevel(CategoryMap categoryMap, Integer sellStatus);
    Goods getUserGoods(String goodsName,Integer categoryId,Integer createUser);
    List<Goods> getUserAllGoods(IdCollection createUser_ids);
}
