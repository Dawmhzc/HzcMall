package com.hzc.demo.controller.mall;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.CategoryMapforMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    //功能正常
    public Result getGoodsList(){
        List<CategoryMapforMap> categoryMapforMapList=categoryService.getCategoryList();//第一层种类的集合
        if (!CollectionUtils.isEmpty(categoryMapforMapList)) {
            CategoryMapforMap categoryMapforMap=categoryMapforMapList.get(0);//第一个第一层种类及下所有的第二层种类的集合
            if (!CollectionUtils.isEmpty(categoryMapforMap.getGoodsCategoryMapList())) {
                CategoryMap categoryMap=categoryMapforMap.getGoodsCategoryMapList().get(0);//第一个二层种类及下所有的第三层种类的集合
                //List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap);

                if (!CollectionUtils.isEmpty(categoryMap.getGoodsCategoryList())) {
                    List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);
                    return new Result(goodsList,0,null);
                }
                //第二层种类下没有种类了
                //获取当前二级的一级种类及子类
                CategoryMap categoryMap1=categoryService.getCategoriesForSearch(categoryMap.getCategoryId());
                List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap1,0);
                return new Result(goodsList,0,null);
            }
            //当前第一层下没有种类了
        }
        return new Result(null,1,"数据不存在");
    }

    @GetMapping("/getGoodsByLevel")
    @ResponseBody
    public Result getGoodsByLevel(@RequestParam("categoryName") String categoryName, @RequestParam("categoryLevel") Integer categoryLevel) {
        if (categoryLevel==null || categoryLevel.equals(0) || !StringUtils.hasLength(categoryName)) {
            System.out.println("参数不合法");
            return new Result(null,1,"参数异常");
        }
        //获取当前选择的种类
        GoodsCategory currentGoodsCategory=categoryService.getCategoryByLevelAndName(categoryLevel, categoryName);
        CategoryMap categoryMap=new CategoryMap();
        categoryMap.copy(currentGoodsCategory);
        //当前种类为二级种类
        if (currentGoodsCategory.getCategoryLevel()==2) {
            List<GoodsCategory> categories=categoryService.getCurrentCategories(currentGoodsCategory.getCategoryId());
            //当前二级下还有种类
            if (!CollectionUtils.isEmpty(categories)) {
                categoryMap.setGoodsCategoryList(categories);
                List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);
                return CollectionUtils.isEmpty(goodsList) ? new Result(null,1,"数据不存在") :
                        new Result(goodsList,0,null);
            }
            //当前二级下没有种类
        }
        //当前种类为三级种类
        //CategoryMap categoryMap=categoryService.getCategoriesForSearch(currentGoodsCategory.getCategoryId());
        List<Goods> goodsList=goodsService.findGoodsByLevel(categoryMap,0);
        return CollectionUtils.isEmpty(goodsList) ? new Result(null,1,"数据不存在") :
                new Result(goodsList,0,null);
    }

    @GetMapping("/getGoodsByKeyword")
    @ResponseBody
    public Result searchGoodsByKeyword(@RequestParam("keyword") String keyword){
        if (!StringUtils.hasLength(keyword)) {
            System.out.println("空指针异常");
            return new Result(null,1,"参数传递不合法");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("keyword",keyword);
        List<Goods> goodsList=goodsService.searchGoodsByKeyword(map);
        return !CollectionUtils.isEmpty(goodsList) ?
                new Result(goodsList,0,null) : new Result(null,1,"查无结果");
    }
}
