package com.hzc.demo.service.impl;

import com.hzc.demo.commom.ErrorEnum;
import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.CategoryMapper;
import com.hzc.demo.dao.GoodsMapper;
import com.hzc.demo.dao.ShopCartMapper;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.IdCollection;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

import static com.hzc.demo.commom.ErrorEnum.INVALID_PARAMS;

/*
 * 除了delGoodsList、downGoods方法其他增删改方法的返回结果1代表成功，0代表失败
 * 该业务层下的所有商品种类id均用goodsCategoryId表示
 * */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    GoodsMapper goodsMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    ShopCartMapper shopCartMapper;

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
    public Result saveGoods(Goods goods) {
        if (goods.hasIllegalField() || goods.hasIllegalMoney()) {
            System.out.println("存在非法字段");
            return new Result(null,1,"传递参数不合法");
            //return 0;
        }
        if (goodsMapper.selectByCategoryIdAndNameAndUser(goods.getGoodsName(),goods.getGoodsCategoryId(),goods.getCreateUser())!=null) {
            System.out.println("数据重复");
            return new Result(null,1,"数据重复");
            //return 0;
        }
        goods.setCreateTime(new Date());
        return goodsMapper.insert(goods)>0 ?
                new Result(null,0,"插入成功"):new Result(null,1,"插入失败");
    }

    //暂时未确定是否要使用批量插入数据
    @Override
    public Result saveGoodsList(List<Goods> goodsList) {
        if (!CollectionUtils.isEmpty(goodsList)) {
            for (Goods goods:goodsList) {
                if (goodsMapper.selectByCategoryIdAndNameAndUser(goods.getGoodsName(),goods.getGoodsCategoryId(),goods.getCreateUser())!=null) {
                    System.out.println("存在数据重复");
                    return new Result(null,1,"数据重复");
                }
                goods.setCreateTime(new Date());
            }
            return goodsMapper.batchInsert(goodsList)>0 ?
                    new Result(null,0,"插入成功"):new Result(null,1,"插入失败");
        }
        return new Result(null,1,"参数不能为空");
    }

    @Override
    public Result editGoods(Goods goods) {
        /*Integer goodsId= (Integer) map.get("goodsId");
        String goodsName = (String) map.get("goodsName");
        Integer goodsCategoryId= (Integer) map.get("goodsCategoryId");
        Integer createUser= (Integer) map.get("createUser");*/
        if (goods.hasIllegalField() || goods.hasIllegalMoney()){
            System.out.println("传入参数不合法");
            return new Result(null,1,"传入参数不合法");
        }
        Goods record=goodsMapper.selectById(goods.getGoodsId());
        if (record==null) {
            System.out.println("数据不存在");
            return new Result(null,1,"数据不存在");
        }
        Goods other=goodsMapper.selectByCategoryIdAndNameAndUser(goods.getGoodsName(),goods.getGoodsCategoryId(),goods.getCreateUser());
        if (other!=null){
            if (goods.equals(other)){
                System.out.println("修改后数据重复");
                return new Result(null,1,"修改后数据重复");
            }
        }
        return goodsMapper.updateById(goods)>0 ?
                new Result(null,0,"修改成功"):new Result(null,1,"修改失败");
    }

    //改变商品上架还是下架，返回数字代表集合中有几条记录不存在，后期写事务考虑全部回滚
    @Override
    public Result downGoods(List<Integer> goodsId_list, Integer sellStatus) {
        Map<String,Object> map=new HashMap<>();
        int row=0;
        if (!CollectionUtils.isEmpty(goodsId_list)) {
            for (Integer id:goodsId_list) {
                if (goodsMapper.selectById(id)!=null) {
                    map.clear();
                    map.put("goodsId",id);
                    map.put("sellStatus",sellStatus);
                    row+=goodsMapper.updateSellStatus(map);
                }
            }
            return row>0 ? new Result(null,0,"成功"):
                    new Result(null,1,"失败");
        }
        System.out.println("集合中没有参数");
        return new Result(null,1,"传递参数不能为空");
    }

    //根据商品id删除商品
    @Override
    public Result delGoods(Integer goodsId) {
        if (goodsId == null) {
            System.out.println("传入参数不合法");
            return new Result(null,1,"传入参数不合法");
        }
        if (goodsMapper.selectById(goodsId)==null) {
            System.out.println("数据不存在");
            return new Result(null,1,"数据不存在");
        }
        return goodsMapper.deleteById(goodsId)>0 ?
                new Result(null,0,"删除成功"):new Result(null,1,"删除失败");
    }

    //返回数字代表集合中有几条记录不存在，后期写事务考虑全部回滚
    @Override
    public Result delGoodsList(List<Integer> goodsId_list) {
        int row=0;
        //用该方式判断是否传递空参不会抛出空指针异常，判断空集合的size会抛出空指针异常
        if (CollectionUtils.isEmpty(goodsId_list)) {
            System.out.println("参数不合法");
            return new Result(null,1,"不能传递空参");
        }
        for (Integer goodsId:goodsId_list){
            if (this.getGoodsById(goodsId)!=null)  row+=goodsMapper.deleteById(goodsId);
        }
        return row>0 ?
                new Result(null,0,"删除成功"):new Result(null,1,"删除失败");
    }

    //需要传入商品关键字keyword、选择的商品种类id(可选)
    @Override
    public List<Goods> searchGoodsByKeyword(Map<String, Object> map) {
        String keyword= (map.get("keyword")+"").trim();
        if (map.containsKey("keyword") && StringUtils.hasLength(keyword)) {
            map.put("keyword",keyword);
            if (map.containsKey("goodsCategoryId") && map.get("goodsCategoryId").equals(0)) {
                System.out.println("传入参数不合法");
                return null;
            }
            map.put("sellStatus",0);
            List<Goods> goodsList=goodsMapper.selectGoodsByKeyword(map);
            return goodsList;
        }
        return null;
    }

    public List<Goods> findGoodsByLevel2(CategoryMap categoryMap) {
        List<Goods> goodsAllList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for (GoodsCategory goodsCategory:categoryMap.getGoodsCategoryList()) {
            map.put("goodsCategoryId",goodsCategory.getCategoryId());
            List<Goods> goodsList=goodsMapper.selectGoodsBySearch(map);
            goodsAllList.addAll(goodsList);
        }
        return goodsAllList;
    }

    //两个函数版本考虑中
    @Override
    public List<Goods> findGoodsByLevel(CategoryMap categoryMap, Integer sellStatus) {
        List<Goods> goodsAllList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("sellStatus",sellStatus);
        //当前categoryMap下没有种类了
        if (CollectionUtils.isEmpty(categoryMap.getGoodsCategoryList())) {
            map.put("goodsCategoryId",categoryMap.getCategoryId());
            goodsAllList=goodsMapper.selectGoodsBySearch(map);
        } else {
            //当前categoryMap下有种类了
            for (GoodsCategory goodsCategory:categoryMap.getGoodsCategoryList()) {
                List<GoodsCategory> goodsCategory_nodes=categoryMapper.selectByParentId(goodsCategory.getCategoryId());
                if (goodsCategory_nodes.size()==0) {
                    System.out.println("该种类下没有分类了");
                    map.put("goodsCategoryId",goodsCategory.getCategoryId());
                    List<Goods> goodsList=goodsMapper.selectGoodsBySearch(map);
                    goodsAllList.addAll(goodsList);
                } else {
                    System.out.println("该种类下还有分类");
                    for (GoodsCategory goodsCategory_node : goodsCategory_nodes) {
                        map.put("goodsCategoryId",goodsCategory_node.getCategoryId());
                        List<Goods> goodsList=goodsMapper.selectGoodsBySearch(map);
                        goodsAllList.addAll(goodsList);
                    }
                }
            }
        }
        return goodsAllList;
    }

    @Override
    public Goods getUserGoods(String goodsName, Integer categoryId, Integer createUser) {
        return goodsMapper.selectByCategoryIdAndNameAndUser(goodsName,categoryId,createUser);
    }

    @Override
    public List<Goods> getUserAllGoods(IdCollection createUser_ids) {
        if (!CollectionUtils.isEmpty(createUser_ids.getIds())){
            List<Integer> ids_list = createUser_ids.getIds();
            String ids= Strings.join(ids_list,',');
            return goodsMapper.selectGoodsByUser(ids);
        }
        return null;
    }

    @Override
    public Result getGoodsByUser(Integer userId) {
        if (userId == null) return Result.fail(INVALID_PARAMS);
        List<Goods> list = goodsMapper.getGoodsByUser(userId);
        return Result.OK(list);
    }
}
