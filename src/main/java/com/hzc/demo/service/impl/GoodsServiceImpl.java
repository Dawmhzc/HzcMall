package com.hzc.demo.service.impl;

import com.hzc.demo.dao.CategoryMapper;
import com.hzc.demo.dao.GoodsMapper;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.service.GoodsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/*
* 除了delGoodsList方法其他增删改方法的返回结果1代表成功，0代表失败
* */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    GoodsMapper goodsMapper;
    @Resource
    CategoryMapper categoryMapper;

    @Override
    public Goods getGoodsById(Integer goodsId) {
        Goods goods=goodsMapper.selectById(goodsId);
        return goods;
    }

    @Override
    public List<Goods> getGoodsList(Map<String,Object> map) {
        if (map.containsKey("ids")){
            List<Integer> ids_list= (List<Integer>) map.get("ids");
            String ids= Strings.join(ids_list,',');
            return goodsMapper.selectGoodsListByIds(ids);
        } else {
            return goodsMapper.selectGoodsBySearch(map);
        }
    }

    @Override
    public int saveGoods(Goods goods) {
        if (goods.hasIllegalField() || goods.hasIllegalMoney()) {
            System.out.println("存在非法字段");
            return 0;
        }
        if (goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(),goods.getGoodsCategoryId())!=null) {
            System.out.println("数据重复");
            return 0;
        }
        return goodsMapper.insert(goods)>0 ? 1:0;
    }

    //暂时未确定是否要使用批量插入数据
    @Override
    public int saveGoodsList(List<Goods> goodsList) {
        if (!CollectionUtils.isEmpty(goodsList)) {
            return goodsMapper.batchInsert(goodsList)>0 ? 1:0;
        }
        System.out.println("数据异常");
        return 0;
    }

    @Override
    public int editGoods(Map<String,Object> map) {
        Integer goodsId= (Integer) map.get("goodsId");
        String goodsName = (String) map.get("goodsName");
        Integer goodsCategoryId= (Integer) map.get("goodsCategoryId");
        if (goodsName==null || goodsCategoryId==null || goodsId==null){
                System.out.println("传入参数不合法");
                return 0;
        }
        //Goods record=goodsMapper.selectById(goodsId);
        if (goodsMapper.selectById(goodsId)==null) {
            System.out.println("数据不存在");
            return 0;
        }
        if (goodsMapper.selectByCategoryIdAndName(goodsName,goodsCategoryId)!=null){
            System.out.println("修改后数据重复");
            return 0;
        }
        return goodsMapper.updateById(map)>0 ? 1:0;
    }

    //根据商品id删除商品
    @Override
    public int delGoods(Integer goodsId) {
        if (goodsId == null) {
            System.out.println("传入参数不合法");
            return 0;
        }
        if (goodsMapper.selectById(goodsId)==null) {
            System.out.println("数据不存在");
            return 0;
        }
        return goodsMapper.deleteById(goodsId)>0 ? 1:0;
    }

    //返回数字代表集合中有几条记录不存在，后期写事务考虑全部回滚
    @Override
    public int delGoodsList(List<Integer> goodsId_list) {
        int row=0;
        if (goodsId_list.size()==0) {
            System.out.println("传入参数不合法");
            return 0;
        }
        for (Integer goodsId:goodsId_list){
            if (goodsId==null || goodsId==0) {
                System.out.println("传入参数不合法");
                return 0;
            }
            if (this.getGoodsById(goodsId)!=null)  row+=goodsMapper.deleteById(goodsId);
        }
        return goodsId_list.size()-row;
    }
}
