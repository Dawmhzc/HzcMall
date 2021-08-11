package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.OrderMapper;
import com.hzc.demo.pojo.Order;
import com.hzc.demo.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

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
        if (order.getOrdAddress().isEmpty() || order.getOrdGoodId() == 0 || order.getOrdShopId() == 0
                || order.getOrdUserId() == 0 || order.getOrdGoodNumb() <= 0)
            return Result.fail(ORDER_ERRER);
        if (order.getOrdStatus() < 1 && order.getOrdStatus() >3)
            return Result.fail(ERROR_STATUS);
        orderMapper.addOrder(order);
        return Result.OK();
    }

    @Override
    public Result listShopOrder(int shopId) {
        List<Order> orderShopList = orderMapper.listShopOrder(shopId);
        return Result.OK(orderShopList);
    }

    @Override
    public Result listAllOrder() {
        List<Order> orderList = orderMapper.listAllOrder();
        return Result.OK(orderList);
    }

    @Override
    public Result getOrder(String ordId) {
        if (ordId.isEmpty()) return  Result.fail(ORRERID_NOTINPUT);
        Order order = orderMapper.getOrder(ordId);
        if (order == null)
            return Result.fail(DATE_NOEXIT);
        return Result.OK(order);
    }

    @Override
    public Result listUserOrder(int userId) {
        List<Order> orderUserList = orderMapper.listUserOrder(userId);
        return Result.OK(orderUserList);
    }

    @Override
    public Result deleteOrder(String ordId) {
        if (orderMapper.deleteOrder(ordId) == 0)
            return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result updateOrder(Order order) {
        if (order.getOrdAddress().isEmpty() || order.getOrdGoodId() == 0 || order.getOrdShopId() == 0
                || order.getOrdUserId() == 0 || order.getOrdGoodNumb() <= 0)
            return Result.fail(ORDER_ERRER);
        if (order.getOrdStatus() < 1 && order.getOrdStatus() >3)
            return Result.fail(ERROR_STATUS);
        if (orderMapper.updateOrder(order) == 0)
            return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }
}
