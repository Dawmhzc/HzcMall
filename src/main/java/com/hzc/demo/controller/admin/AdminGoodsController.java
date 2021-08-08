package com.hzc.demo.controller.admin;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.IdCollection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminGoodsController {

    @Resource
    GoodsService goodsService;
    @Resource
    CategoryService categoryService;

    @GetMapping("/getAllGoods")
    public String getAllGoods(Model model){
        List<Goods> goodsList=goodsService.findGoodsByLevel(new CategoryMap(),null);
        model.addAttribute("goodsList",goodsList);
        return "adminGoods";
    }

    @GetMapping("/getGoodsDetail/{goodsId}")
    public String getGoodsDetail(@PathVariable("goodsId") Integer goodsId,Model model){
        model.addAttribute("goods",goodsService.getGoodsById(goodsId));
        return "showGoods";
    }

    @GetMapping("/goAddGoods")
    public String goAddGoods(){
        return "addGoods";
    }

    @PostMapping("/saveGoods")
    @ResponseBody
    public Result saveGoods(@RequestParam("goodsName") String goodsName,
                            @RequestParam("goodsIntro") String goodsIntro,
                            @RequestParam("goodsCategoryId") Integer goodsCategoryId,
                            @RequestParam("goodsImg") String goodsImg,
                            @RequestParam("originalPrice") Double originalPrice,
                            @RequestParam("sellingPrice") Double sellingPrice,
                            @RequestParam("stockNum") Integer stockNum,
                            @RequestParam("createUser") Integer createUser){
        Goods goods=new Goods();
        goods.setGoodsName(goodsName);
        goods.setGoodsCategoryId(goodsCategoryId);
        goods.setGoodsImg(goodsImg);
        goods.setGoodsIntro(goodsIntro);
        goods.setOriginalPrice(originalPrice);
        goods.setStockNum(stockNum);
        goods.setSellStatus(0);
        goods.setSellingPrice(sellingPrice);
        goods.setCreateUser(createUser);
        return goodsService.saveGoods(goods);
    }

    @PostMapping("/updateGoods")
    @ResponseBody
    public Result updateGoods(@RequestParam("goodsId") Integer goodsId,
                              @RequestParam("goodsName") String goodsName,
                              @RequestParam("goodsCategoryId") Integer goodsCategoryId,
                              @RequestParam("createUser") Integer createUser){
        Map<String,Object> map=new HashMap<>();
        map.put("goodsId",goodsId);
        map.put("goodsName",goodsName);
        map.put("goodsCategoryId",goodsCategoryId);
        map.put("createUser",createUser);
        return goodsService.editGoods(map);
    }

    @PostMapping("/delGoods")
    @ResponseBody
    public Result delGoods(IdCollection idCollection){
        return goodsService.delGoodsList(idCollection.getIds());
    }

    @PostMapping("/downGoods")
    @ResponseBody
    public Result downGoods(IdCollection idCollection){
        System.out.println(idCollection.getIds());
        return goodsService.downGoods(idCollection.getIds(),1);
    }

    @PostMapping("/upGoods")
    @ResponseBody
    public Result upGoods(IdCollection idCollection){
        System.out.println(idCollection.getIds());
        return goodsService.downGoods(idCollection.getIds(),0);
    }
}
