package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Order;

public interface OrderService {
    /**
     * 获取某订单
     * @param ordId  单号
     * @return
     */
    Result getOrder(String  ordId);

    /**
     * 获取用户所有订单
     * @param userId 用户id
     * @return
     */
    Result listUserOrder(int userId);

    /**
     * 删除订单
     * @param ordId 订号
     * @return
     */
    Result deleteOrder(String ordId);

    /**
     * 更新订单信息
     * @param order 修改后订单信息
     * @return
     */
    Result updateOrder(Order order);

    /**
     * 提交订单
     * @param order 订单信息
     * @return
     */
    Result addOrder(Order order);

    /**
     * 查询店铺订单
     * @param shopId  店铺账号id
     * @return
     */
    Result listShopOrder(int shopId);

    /**
     * 后台管理全部订单信息
     * @return
     */
    Result listAllOrder();

    /**
     * 购物特定商家推荐
     * @return
     */
    Result wishList(int userId);
}
