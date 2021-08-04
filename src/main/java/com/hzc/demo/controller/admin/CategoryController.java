package com.hzc.demo.controller.admin;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Resource
    GoodsService goodsService;
    @Resource
    CategoryService categoryService;

    /*
    * @RequestBody只适用于获取请求主体中的参数不能获取url上的参数
    * @RequestParam 底层是通过request.getParameter方式获得参数的，换句话说，@RequestParam 和request.getParameter是同一回事
    * @RequestBody接受的是一个json对象的字符串，而不是Json对象
    * */
    @PostMapping("/saveCategory")
    @ResponseBody
    public Result saveCategory(@RequestParam("categoryLevel") Integer categoryLevel,
                               @RequestParam("parentId") Integer parentId,
                               @RequestParam("categoryName") String categoryName) {
        GoodsCategory goodsCategory=new GoodsCategory();
        goodsCategory.setCategoryName(categoryName);
        goodsCategory.setCategoryLevel(categoryLevel);
        goodsCategory.setParentId(parentId);
        System.out.println(goodsCategory);
        return categoryService.saveCategory(goodsCategory)>0 ? new Result(goodsCategory,0,null) :
                new Result(null,1,"插入数据失败");
    }

    @GetMapping("/delCategory")
    @ResponseBody
    public Result delCategory(@RequestParam("categoryId") Integer categoryId){
        return categoryService.delCateGory(categoryId)>0 ? new Result(null,0,"删除成功") :
                new Result(null,1,"删除失败");
    }

    @PostMapping("/updateCategory")
    @ResponseBody
    public Result updateCategory(@RequestParam("categoryLevel") Integer categoryLevel,
                                 @RequestParam("categoryId") Integer categoryId,
                                 @RequestParam("categoryName") String categoryName){
        Map<String,Object> map=new HashMap<>();
        map.put("categoryLevel",categoryLevel);
        map.put("categoryId",categoryId);
        map.put("categoryName",categoryName);
        return categoryService.updateCateGory(map)>0 ? new Result(null,0,"修改成功") :
                new Result(null,1,"修改失败");
    }

    @GetMapping("/getAllCategory/{categoryLevel}")
    public String getAllCategory(Model model,@PathVariable("categoryLevel") Integer categoryLevel){
        model.addAttribute("categories",categoryService.getCategoriesByLevel(categoryLevel));
        return "category";
    }

    @GetMapping("/getCurrentCategory/{categoryId}")
    //@ResponseBody
    public String getCurrentCategory(Model model, @PathVariable("categoryId") Integer categoryId){
        List<GoodsCategory> goodsCategories=categoryService.getCurrentCategories(categoryId);
        model.addAttribute("categories",goodsCategories);
        //return new Result(goodsCategories,0,null);
        return "category";
    }
}
