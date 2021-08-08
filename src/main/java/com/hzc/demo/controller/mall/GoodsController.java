package com.hzc.demo.controller.mall;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.CategoryMapforMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//未测试
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    GoodsService goodsService;
    @Resource
    CategoryService categoryService;

    @GetMapping("/getGoodsList")
    //@ResponseBody
    //功能正常,初始加载页面使用
    public String getGoodsList(Model model){
        List<CategoryMapforMap> categoryMapforMapList=categoryService.getCategoryList();//第一层种类的集合包括子孙类
        if (!CollectionUtils.isEmpty(categoryMapforMapList)) {
            CategoryMapforMap categoryMapforMap=categoryMapforMapList.get(0);//第一个第一层种类及下所有的第二层种类的集合
            if (!CollectionUtils.isEmpty(categoryMapforMap.getGoodsCategoryMapList())) {
                CategoryMap categoryMap=categoryMapforMap.getGoodsCategoryMapList().get(0);//第一个二层种类及下所有的第三层种类的集合

                if (!CollectionUtils.isEmpty(categoryMap.getGoodsCategoryList())) {
                    List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);//获取当前二级下的所有商品
                    model.addAttribute("goodsList",goodsList);
                    model.addAttribute("currentCategories",categoryMapforMapList);
                    model.addAttribute("parentCategory",null);
                    return "mall";
                }
                return "mall";
            }
            //当前第一层下没有种类了
        }
        return "mall";
    }

    @GetMapping("/getGoodsByLevel/{categoryName}/{categoryLevel}")
    //@ResponseBody
    public String getGoodsByLevel(Model model, @PathVariable("categoryName") String categoryName, @PathVariable("categoryLevel") Integer categoryLevel) {
        if (categoryLevel==null || categoryLevel.equals(0) || !StringUtils.hasLength(categoryName)) {
            System.out.println("参数不合法");
            return "mall";
        }

        //获取当前选择的种类
        GoodsCategory currentGoodsCategory=categoryService.getCategoryByLevelAndName(categoryLevel, categoryName);
        CategoryMap categoryMap=new CategoryMap();
        categoryMap.copy(currentGoodsCategory);

        //当前种类为一级种类
        if (currentGoodsCategory.getCategoryLevel()==1) {
            //获取当前种类下的所有下级商品种类
            List<GoodsCategory> categories=categoryService.getCurrentCategories(currentGoodsCategory.getCategoryId());
            categoryMap.setGoodsCategoryList(categories);
            //获取当前种类下的所有商品
            List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);
            model.addAttribute("goodsList",goodsList);
            model.addAttribute("parentCategory",currentGoodsCategory);
            model.addAttribute("currentCategories",categories);
            return "mall";
        }

        //当前种类为二级种类
        if (currentGoodsCategory.getCategoryLevel()==2) {
            //获取当前种类下的子种类
            List<GoodsCategory> categories=categoryService.getCurrentCategories(currentGoodsCategory.getCategoryId());
            //当前二级下还有种类
            if (!CollectionUtils.isEmpty(categories)) {
                categoryMap.setGoodsCategoryList(categories);
                //获取当前二级种类下的商品
                List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);
                model.addAttribute("goodsList",goodsList);
                model.addAttribute("parentCategory",currentGoodsCategory);
                model.addAttribute("currentCategories",categories);
                return "mall";
            }
            //当前二级下没有种类
        }

        List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);
        //获得当前种类的父节点及同父节点的子类
        CategoryMap secondCategory=categoryService.getCategoriesForSearch(currentGoodsCategory.getCategoryId());
        model.addAttribute("goodsList",goodsList);

        model.addAttribute("currentCategories",secondCategory.getGoodsCategoryList());
        return "mall";

    }

    @PostMapping("/getGoodsByKeyword")
    //@ResponseBody
    public String searchGoodsByKeyword(Model model, @RequestParam String keyword){
        if (!StringUtils.hasLength(keyword)) {
            System.out.println("空指针异常");
            //return new Result(null,1,"参数传递不合法");
            return "mall";
        }
        Map<String,Object> map=new HashMap<>();
        map.put("keyword",keyword);
        List<Goods> goodsList=goodsService.searchGoodsByKeyword(map);
        CategoryMap categoryMap=categoryService.getCategoriesForSearch(goodsList.get(0).getGoodsCategoryId());
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("parentCategory",categoryMap);
        model.addAttribute("currentCategories",categoryMap.getGoodsCategoryList());
        //return !CollectionUtils.isEmpty(goodsList) ? new Result(goodsList,0,null) : new Result(null,1,"查无结果");
        return "mall";
    }

    @GetMapping("/getDetailMallGoods/{goodsId}")
    public String getDetailMallGoods(Model model, @PathVariable Integer goodsId){
        if (goodsId==null || goodsId.equals(0)) {
            System.out.println("传递参数不合法");
            return "showMallGoods";
        }
        Goods DetailGoods=goodsService.getGoodsById(goodsId);
        model.addAttribute("detailGoods",DetailGoods);
        return "showMallGoods";
    }
}
