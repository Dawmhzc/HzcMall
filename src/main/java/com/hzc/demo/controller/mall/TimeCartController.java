package com.hzc.demo.controller.mall;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.pojo.TimeCart;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.service.TimeCartService;
import com.hzc.demo.util.GetRandom;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hzc.demo.commom.ErrorEnum.LOGIN_STATE;

@Controller
@RequestMapping("/time")
public class TimeCartController {
    @Resource
    private TimeCartService timeCartServiceImpl;
    @Resource
    private GoodsService goodsServiceImpl;

    @RequestMapping("/all")
    public String listTimeCart(Map<String,Object> map1, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            map1.put("message","未登录");
            return "mall/404page";
        }
        Result result = timeCartServiceImpl.listTimeCart(userId);
        if (result.getCode() != 0){
            map1.put("message",result.getMessage());
            return "mall/404page";
        }
        List<TimeCart> cartList = (List<TimeCart>) result.getData();
        List<Map<String,Object>> list = new ArrayList<>();
        for (TimeCart timeCart:cartList) {
            Goods goods=goodsServiceImpl.getGoodsById(timeCart.getGoodsId());
            if (goods != null) {
                BigDecimal price = BigDecimal.valueOf(timeCart.getGoodsNumb()*goods.getSellingPrice());
                Map<String,Object> map=new HashMap<>();
                map.put("id",timeCart.getId());
                map.put("goodsName",goods.getGoodsName());
                map.put("goodsCount",timeCart.getGoodsNumb());
                map.put("total",price);
                map.put("goTime",timeCart.getGoTime());
                list.add(map);
            }
        }
        map1.put("timeCartList",list);
        return "mall/TimeCart";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result addTimeCart(@RequestParam("goodsId") Integer goodsId,
                              @RequestParam("goodsCount") Integer goodsCount,
                              HttpSession session){
        String address = (String) session.getAttribute("address");
        String mobile = (String) session.getAttribute("mobile");
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return Result.fail(LOGIN_STATE);
        }
        TimeCart timeCart = new TimeCart();
        timeCart.setUserId(userId);
        timeCart.setGoodsId(goodsId);
        timeCart.setGoodsNumb(goodsCount);
        timeCart.setAddress(address);
        timeCart.setMobile(mobile);
        timeCart.setGoTime("未设置");
        timeCart.setState(0);
        return Result.OK(timeCartServiceImpl.addTimeCart(timeCart));
    }

    @RequestMapping("/del")
    public String delTimeCart(int id, Map<String,Object> map){
        Result result = timeCartServiceImpl.delTimeCart(id);
        if (result.getCode()!=0){
            map.put("message",result.getMessage());
            return "mall/404page";
        }
        return "redirect:/time/all";
    }

    @RequestMapping("/set")
    public String getTimeCart(int id, Map<String,Object> map){
        TimeCart timeCart = (TimeCart) timeCartServiceImpl.getTimeCart(id).getData();
        if (timeCart==null) {
            map.put("message","查询不到数据");
            return "mall/404page";
        }
        Goods goods=goodsServiceImpl.getGoodsById(timeCart.getGoodsId());
        map.put("id",timeCart.getId());
        map.put("goodsName",goods.getGoodsName());
        map.put("goodsCount",timeCart.getGoodsNumb());
        map.put("address",timeCart.getAddress());
        map.put("mobile",timeCart.getMobile());
        map.put("goTime",timeCart.getGoTime());
        map.put("state",timeCart.getState());
        return "mall/setwish";
    }

    @RequestMapping("/upd")
    public String updTimeCart(@RequestParam("goodsCount") int goodsCount,
                              @RequestParam("address") String address,
                              @RequestParam("mobile") String mobile,
                              @RequestParam("goTime") String goTime,
                              @RequestParam("id") int id, Map<String,Object> map){
        TimeCart timeCart = (TimeCart) timeCartServiceImpl.getTimeCart(id).getData();
        if (timeCart==null) {
            map.put("message","查询不到数据");
            return "mall/404page";
        }
        timeCart.setGoodsNumb(goodsCount);
        timeCart.setAddress(address);
        timeCart.setMobile(mobile);
        timeCart.setGoTime(goTime);
        if (goTime.equals("未设置"))
            timeCart.setState(0);
        if (goTime.equals("按周购买"))
            timeCart.setState(1);
        if (goTime.equals("按月购买"))
            timeCart.setState(2);
        Result result = timeCartServiceImpl.updTimeCart(timeCart);
        if (result.getCode()!=0) {
            map.put("message",result.getMessage());
            return "mall/404page";
        }
        return "redirect:/time/all";
    }

    @RequestMapping("/toPay")
    public String goPay(int id, HttpServletRequest request,HttpSession session){
        TimeCart timeCart = (TimeCart) timeCartServiceImpl.getTimeCart(id).getData();
        Goods goods = goodsServiceImpl.getGoodsById(timeCart.getGoodsId());
        String sellingPrice = goods.getSellingPrice().toString();
        String goodsCount = timeCart.getGoodsNumb().toString();
        BigDecimal numb = new BigDecimal(goodsCount);
        BigDecimal selPrice = new BigDecimal(sellingPrice);
        BigDecimal price = numb.multiply(selPrice);
        request.setAttribute("ordId", GetRandom.getRandomId());
        request.setAttribute("goods",goods);
        request.setAttribute("cartNumb",timeCart.getGoodsNumb());
        request.setAttribute("price",price);
        request.setAttribute("address",timeCart.getAddress());
        request.setAttribute("mobile",timeCart.getMobile());
        session.setAttribute("timeCart",timeCart);
        return "forward:/order/indate";
    }
}
