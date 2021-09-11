package com.hzc.demo.controller.mall;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.pojo.PriceSurge;
import com.hzc.demo.pojo.ShopCart;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.service.PriceSurgeService;
import com.hzc.demo.service.ShopCartService;
import com.hzc.demo.util.IdCollection;
import com.hzc.demo.util.GetRandom;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

import static com.hzc.demo.commom.ErrorEnum.LOGIN_STATE;

@Controller
@RequestMapping("/shopCart")
public class ShopCartController {

    @Resource
    ShopCartService shopCartService;
    @Resource
    GoodsService goodsService;
    @Resource
    PriceSurgeService priceSurgeService;

    @GetMapping("/goShopCart")
    public String goShopCart(){
        return "mall/cart";
    }

    @GetMapping("/showShopCartList")
    public String showShopCartList(Model model,HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            model.addAttribute("message","未登录");
            return "mall/404page";
        }
        List<ShopCart> shopCartList= shopCartService.selectShopCartList(userId);
        List<Map<String,Object>> shopCarts=new ArrayList<>();
        for (ShopCart shopCart:shopCartList) {
            Goods goods=goodsService.getGoodsById(shopCart.getGoodsId());
            Map<String,Object> map=new HashMap<>();
            map.put("shopCartId",shopCart.getShopCartId());
            map.put("goodsId",goods.getGoodsId());
            map.put("goodsName",goods.getGoodsName());
            map.put("sellingPrice",goods.getSellingPrice());
            map.put("goodsCount",shopCart.getGoodsCount());
            map.put("Total",shopCart.getGoodsCount()*goods.getSellingPrice());
            shopCarts.add(map);
        }
        model.addAttribute("shopCartList",shopCarts);
        return "mall/cart";
    }

    @GetMapping("/getDetailMallGoods/{goodsId}")
    public String getDetailShopCart(Model model, @PathVariable Integer goodsId){
        Goods goods=goodsService.getGoodsById(goodsId);
        if (goods==null) return "mall/404page";
        model.addAttribute("goods",goods);
        return "mall/GoodsDetail";
    }

    @PostMapping("/editShopCart")
    @ResponseBody
    public Result editShopCart(@RequestParam("shopCartId") Integer shopCartId,
                               @RequestParam("goodsCount") Integer goodsCount) {
        Map<String,Object> map=new HashMap<>();
        map.put("goodsCount",goodsCount);
        map.put("shopCartId",shopCartId);
        return shopCartService.editShopCart(map);
    }

    @PostMapping("/delShopCart")
    @ResponseBody
    public Result delShopCart(IdCollection idCollection){
        return shopCartService.delShopCart(idCollection.getIds());
    }

    @PostMapping("/addShopCart")
    @ResponseBody
    public Result addShopCart(HttpSession session,
                              @RequestParam("goodsId") Integer goodsId,
                              @RequestParam("goodsCount") Integer goodsCount){
        ShopCart shopCart=new ShopCart();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return Result.fail(LOGIN_STATE);
        }
        shopCart.setUserId(userId);
        shopCart.setGoodsId(goodsId);
        shopCart.setGoodsCount(goodsCount);
        shopCart.setCreateTime(new Date());
        System.out.println(shopCart);
        return shopCartService.saveShopCart(shopCart);
    }

    @PostMapping("/getTatal")
    @ResponseBody
    public Result getTatal(IdCollection idCollection){
        Double total=0.0;
        for (Integer id:idCollection.getIds()) {
            ShopCart shopCart=shopCartService.selectShopCartByKey(id);
            total+=(goodsService.getGoodsById(shopCart.getGoodsId()).getSellingPrice())*(shopCart.getGoodsCount());
        }
        return new Result(total,0,null);
    }

    @GetMapping("/getSurge/{goodsId}")
    //@ResponseBody
    public String getSurge(Model model, @PathVariable Integer goodsId){
        PriceSurge priceSurge=priceSurgeService.getSurge(goodsId);
        Goods goods=goodsService.getGoodsById(goodsId);
        List<Double> surgeList= new ArrayList<>();
        if (priceSurge==null) {
            model.addAttribute("message","数据不存在");
            return "mall/404page";
        }
        List<String> temp=Arrays.asList(priceSurge.getSurge().split(","));
        for (String surge:temp) {
            surgeList.add(Double.parseDouble(surge));
        }
        int[] size=new int[surgeList.size()];
        for (int i=0;i<surgeList.size();i++){
            size[i]=i+1;
        }
        model.addAttribute("name",goods.getGoodsName());
        model.addAttribute("value",surgeList);
        model.addAttribute("size",size);
        //return new Result(viewData,0,null);
        return "mall/showViewData";
    }

    //跳转app-ecommerce-checkout
    @GetMapping("/goCheckout/{shopCartId}")
    public String goCheckout(HttpServletRequest request,@PathVariable Integer shopCartId){
        ShopCart shopCart=shopCartService.selectShopCartByKey(shopCartId);
        Goods goods=goodsService.getGoodsById(shopCart.getGoodsId());
        if (goods==null) return "mall/404page";
        String id = GetRandom.getRandomId();
        String address = (String) request.getSession().getAttribute("address");
        String mobile = (String) request.getSession().getAttribute("mobile");
        if (address == null || mobile == null || address.isEmpty() || mobile.isEmpty()){
            return "redirect:/address/all";
        }
        Double total = goods.getSellingPrice()*shopCart.getGoodsCount();
        BigDecimal price = BigDecimal.valueOf(total);
        request.setAttribute("ordId",id);
        request.setAttribute("goods",goods);
        request.setAttribute("cartNumb",shopCart.getGoodsCount());
        request.setAttribute("price",price);
        request.setAttribute("address",address);
        request.setAttribute("mobile",mobile);
        return "forward:/order/indate";
    }
}
