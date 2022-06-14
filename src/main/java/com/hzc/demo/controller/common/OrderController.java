package com.hzc.demo.controller.common;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.*;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.service.OrderService;
import com.hzc.demo.service.TimeCartService;
import com.hzc.demo.service.UserService;
import com.hzc.demo.util.IdCollection;
import com.hzc.demo.util.GetRandom;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderServiceImpl;
    @Resource
    private GoodsService goodsServiceImpl;
    @Resource
    private TimeCartService timeCartServiceImpl;
    @Resource
    private UserService userServiceImpl;

    @RequestMapping("/getall")
    public String listUserOrder(HttpSession session, Map<String,Object> map){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            map.put("message","未登录");
            return "mall/404page";
        }
        List<Order> orderList = (List<Order>) orderServiceImpl.listUserOrder(userId).getData();
        map.put("orderList",orderList);
        return "mall/order";
    }

    @RequestMapping("/indate")
    public String invalid(HttpServletRequest request,HttpSession session){
        String address = (String) request.getAttribute("address");
        String mobile = (String) request.getAttribute("mobile");
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Goods goods = (Goods) request.getAttribute("goods");
        Integer goodsNumb = (Integer) request.getAttribute("cartNumb");
        BigDecimal price = (BigDecimal) request.getAttribute("price");
        Order order = new Order();
        order.setOrdId(GetRandom.getRandomId());
        order.setOrdUserId(userId);
        order.setOrdAddress(address);
        order.setOrdMobile(mobile);
        order.setOrdTotal(price);
        order.setOrdGoodName(goods.getGoodsName());
        order.setOrdGoodNumb(goodsNumb);
        order.setOrdShopId(goods.getCreateUser());
        order.setOrdBuyTime(new Date());
        session.setAttribute("order",order);
        return "forward:/alipay";
    }

    @RequestMapping("/add")
    public String addOrder(HttpSession session) throws UnsupportedEncodingException {
        Order order = (Order) session.getAttribute("order");
        TimeCart timeCart = (TimeCart) session.getAttribute("timeCart");
        orderServiceImpl.addOrder(order);
        if (timeCart != null){
            if (timeCart.getGoTime().equals("按月购买")){
                timeCart.setState(2);
                timeCartServiceImpl.updTimeCart(timeCart);
            }else if (timeCart.getGoTime().equals("按周购买")){
                timeCart.setState(1);
                timeCartServiceImpl.updTimeCart(timeCart);
            }
            session.removeAttribute("timeCart");
        }
        return "redirect:/order/getall";
    }

    @RequestMapping("/admin/all")
    public String listShopAll(HttpSession session, Map<String,Object> map){
        Integer shopId = (Integer) session.getAttribute("userId");
        if (shopId == null) {
            map.put("message","未登录");
            return "Admin/auth-404";
        }
        List<Order> orderList = (List<Order>) orderServiceImpl.listShopOrder(shopId).getData();
        map.put("orderList",orderList);
        return "Admin/AdminOrder";
    }

    @RequestMapping("/admin/upd")
    public String updOrder(Order order,Map<String,Object> map){
        Result result = orderServiceImpl.updateOrder(order);
        if (result.getCode() != 0){
            map.put("message",result.getMessage());
            return "Admin/auth-404";
        }
        return "redirect:/order/admin/all";
    }

    @RequestMapping("/admin/delete")
    public String delOrder(String orderId){
        orderServiceImpl.deleteOrder(orderId);
        return "redirect:/order/admin/all";
    }

    @RequestMapping("/wish")
    public String wishList(Map<String,Object> map,HttpSession session){
        IdCollection idCollection = new IdCollection();
        Integer id = (Integer) session.getAttribute("userId");
        if (id == null) {
            map.put("message","未登录");
            return "mall/404page";
        }
        List<Integer> list = (List<Integer>) orderServiceImpl.wishList(id).getData();
        idCollection.setIds(list);
        List<Goods> userAllGoods = goodsServiceImpl.getUserAllGoods(idCollection);
        List<User> userList=new ArrayList<>();
        for (Integer userId:list) {
            User user= (User) userServiceImpl.getUser(userId).getData();
            if (user!=null) userList.add(user);
        }
        System.out.println(userList);
        map.put("goodsList",userAllGoods);
        map.put("courses",userList);
        return "mall/goods2";
    }

    @GetMapping("/getSortedGoods/{createUser}")
    public String getSortedGoods(Map<String,Object> map,@PathVariable Integer createUser,HttpSession session){
        IdCollection idCollection=new IdCollection();
        List<Integer> ids=new ArrayList<>();
        ids.add(createUser);
        idCollection.setIds(ids);
        List<Goods> goodsList=goodsServiceImpl.getUserAllGoods(idCollection);
        Integer id = (Integer) session.getAttribute("userId");
        if (id == null) {
            map.put("message","未登录");
            return "mall/404page";
        }
        List<Integer> list = (List<Integer>) orderServiceImpl.wishList(id).getData();
        List<User> userList=new ArrayList<>();
        for (Integer userId:list) {
            User user= (User) userServiceImpl.getUser(userId).getData();
            if (user!=null) {
                if (user.getUserId().equals(createUser)) {
                    userList.add(0,user);
                    continue;
                }
                userList.add(user);
            }
        }
        System.out.println(userList);
        map.put("goodsList",goodsList);
        map.put("courses",userList);
        return "mall/goods2";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result getOrder(@RequestBody OrderDto id){
        return orderServiceImpl.getOrder(id.getId());
    }
}
