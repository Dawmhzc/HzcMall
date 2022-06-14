package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.ShopCart;

import java.util.List;
import java.util.Map;

public interface ShopCartService {

    Result saveShopCart(ShopCart shopCart);
    Result editShopCart(Map<String,Object> map);
    Result delShopCart(List<Integer> ids);
    ShopCart selectShopCartByKey(Integer shopCartId);
    List<ShopCart> selectShopCartList(int userId);
}
