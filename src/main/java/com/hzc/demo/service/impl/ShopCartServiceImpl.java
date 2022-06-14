package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.ShopCartMapper;
import com.hzc.demo.pojo.ShopCart;
import com.hzc.demo.service.ShopCartService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Resource
    ShopCartMapper shopCartMapper;

    @Override
    public Result saveShopCart(ShopCart shopCart) {
        if (shopCart.hasIllegalField()) {
            System.out.println("存在非法字段");
            return new Result(null,1,"存在非法字段");
        }
        return shopCartMapper.saveShopCart(shopCart)>0 ? new Result(null,0,"插入成功"):
                new Result(null,1,"插入失败");
    }

    @Override
    public Result editShopCart(Map<String, Object> map) {
        if (!map.containsKey("shopCartId") ||
                (!map.containsKey("goodsCount") && !map.containsKey("goodsId") && !map.containsKey("userId"))){
            System.out.println("不能传递空参");
            return new Result(null,1,"不能传递空参");
        }
        if (shopCartMapper.selectShopCartById((Integer) map.get("shopCartId"))==null) {
            System.out.println("数据不存在");
            return new Result(null,1,"数据不存在");
        }
        return shopCartMapper.updateShopCart(map)>0 ? new Result(null,0,"修改成功"):
                new Result(null,1,"修改失败");
    }

    @Override
    public Result delShopCart(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            System.out.println("参数不合法");
            return new Result(null,1,"不能传递空参");
        }
        int row=0;
        for (Integer shopCartId:ids) {
            if (shopCartId==null || shopCartId.equals(0)) {
                System.out.println("传入参数不合法");
                return new Result(null,1,"传入参数不合法");
            }
            row+=shopCartMapper.delShopCart(shopCartId);
        }
        return row>0 ? new Result(null,0,"删除成功")
                :new Result(null,1,"删除失败");
    }

    @Override
    public ShopCart selectShopCartByKey(Integer shopCartId) {
        return shopCartMapper.selectShopCartById(shopCartId);
    }

    @Override
    public List<ShopCart> selectShopCartList(int userId) {
        return shopCartMapper.selectShopCartList(userId);
    }
}
