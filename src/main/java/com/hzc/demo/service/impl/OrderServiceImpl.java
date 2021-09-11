package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.OrderMapper;
import com.hzc.demo.pojo.Order;
import com.hzc.demo.service.OrderService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hzc.demo.commom.ErrorEnum.*;

/**
 * @author hzc
 * @date 2021.8.11
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Override
    public Result addOrder(Order order) {
        if (order.getOrdAddress().isEmpty() || order.getOrdGoodName().isEmpty() || order.getOrdShopId() == null
                || order.getOrdUserId() == null || order.getOrdGoodNumb() <= 0)
            return Result.fail(ORDER_ERRER);
        orderMapper.addOrder(order);
        return Result.OK();
    }

    @Override
    public Result listShopOrder(int shopId) {
        List<Order> orderShopList = orderMapper.listShopOrder(shopId);
        if (orderShopList == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(orderShopList);
    }

    @Override
    public Result listAllOrder() {
        List<Order> orderList = orderMapper.listAllOrder();
        if (orderList == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(orderList);
    }

    @Override
    public Result wishList(int userId) {
        int index = 0;
        int count = 0;
        List<Order> orderList = orderMapper.listUserOrder(userId);
        Map<Integer,Order> map = new HashMap<>();
        Map<Integer,Integer> map1 = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        for (Order order:orderList) {
            Integer ordShopId = order.getOrdShopId();
            map.put(ordShopId,order);
            count++;
            map1.putIfAbsent(ordShopId, index);
            Integer integer = map1.get(ordShopId);
            integer++;
            map1.replace(ordShopId,integer);
        }
        for (Map.Entry<Integer,Integer> entry :map1.entrySet()){
            Integer value = entry.getValue();
            Integer shopId = entry.getKey();
            if ((float)value / count >= 0.2)
                list.add(shopId);
        }
        return Result.OK(list);
    }

    @Override
    public Result getOrder(String ordId) {
        if (ordId.isEmpty()) return Result.fail(INVALID_PARAMS);
        Order order = orderMapper.getOrder(ordId);
        if (order == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(order);
    }

    @Override
    public Result listUserOrder(int userId) {
        List<Order> orderUserList = orderMapper.listUserOrder(userId);
        if (orderUserList == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(orderUserList);
    }

    @Override
    public Result deleteOrder(String ordId) {
        if (orderMapper.deleteOrder(ordId) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result updateOrder(Order order) {
        if (order.getOrdAddress().isEmpty() || order.getOrdGoodName().isEmpty() || order.getOrdShopId() == null
                || order.getOrdUserId() == null || order.getOrdGoodNumb() <= 0)
            return Result.fail(ORDER_ERRER);
        if (orderMapper.updateOrder(order) == 0)
            return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }


}
