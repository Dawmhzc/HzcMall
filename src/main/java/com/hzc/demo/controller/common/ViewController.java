package com.hzc.demo.controller.common;


import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Order;
import com.hzc.demo.pojo.User;
import com.hzc.demo.service.OrderService;
import com.hzc.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class ViewController {
    @Resource
    private UserService userServiceImpl;
    @Resource
    private OrderService orderServiceImpl;
    @RequestMapping("/")
    public String index(){
        return "mall/index";
    }

    @RequestMapping("/login")
    public String login(){
        return "mall/login";
    }

    @RequestMapping("/admin/login")
    public String adminLogin(){
        return "Admin/auth-login";
    }

    @RequestMapping("/admin/updUser")
    public String toUpd(int id, Map<String,Object> map){
        Result result = userServiceImpl.getUser(id);
        if (result.getCode() != 0){
            map.put("message",result.getMessage());
            return "Admin/auth-404";
        }
        User user = (User) result.getData();
        map.put("userId",user.getUserId());
        map.put("userName",user.getUserName());
        map.put("userPwd",user.getUserPwd());
        map.put("userMobile",user.getUserMobile());
        map.put("userEmail",user.getUserEmail());
        map.put("userRight",user.getUserRight());
        return "Admin/UpdUser";
    }

    @RequestMapping("/admin/updOrder")
    public String toUpdOrder(String id, Map<String,Object> map){
        Result result = orderServiceImpl.getOrder(id);
        if (result.getCode() != 0 ){
            map.put("message",result.getMessage());
            return "Admin/auth-404";
        }
        Order order = (Order) result.getData();
        map.put("ordId",order.getOrdId());
        map.put("ordUserId",order.getOrdUserId());
        map.put("ordAddress",order.getOrdAddress());
        map.put("ordBuyTime",order.getOrdBuyTime());
        map.put("ordTotal",order.getOrdTotal());
        map.put("ordGoodsName",order.getOrdGoodName());
        map.put("ordGoodsNumb",order.getOrdGoodNumb());
        map.put("ordShopId",order.getOrdShopId());
        map.put("ordMobile",order.getOrdMobile());
        return "Admin/UpdOrder";
    }
}
