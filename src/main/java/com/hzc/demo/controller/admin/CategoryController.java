package com.hzc.demo.controller.admin;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.util.IdCollection;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
* 该控制层做了修改
* */
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
                               @Nullable @RequestParam("parentId") Integer parentId,
                               @RequestParam("categoryName") String categoryName) {
        GoodsCategory goodsCategory=new GoodsCategory();
        goodsCategory.setCategoryName(categoryName);
        goodsCategory.setCategoryLevel(categoryLevel);
        goodsCategory.setParentId(parentId);
        System.out.println(goodsCategory);
        return categoryService.saveCategory(goodsCategory);
    }

    @PostMapping("/delCategory")
    @ResponseBody
    public Result delCategory(IdCollection idCollection){
        return categoryService.delCateGory(idCollection.getIds());
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
        return categoryService.updateCateGory(map);
    }

    @GetMapping("/goAddCategory")
    public String goAddCategory(){
        return "Admin/addCategory";
    }

    @GetMapping("/getAllCategory/{categoryLevel}")
    public String getAllCategory(Model model,@PathVariable("categoryLevel") Integer categoryLevel){
        model.addAttribute("categories",categoryService.getCategoriesByLevel(categoryLevel));
        return "Admin/AdminCategory";
    }

    @GetMapping("/getCurrentCategory/{categoryId}")
    //@ResponseBody
    public String getCurrentCategory(Model model, @PathVariable("categoryId") Integer categoryId){
        List<GoodsCategory> goodsCategories=categoryService.getCurrentCategories(categoryId);
        GoodsCategory parentCategory=categoryService.getCategoryById(categoryId);
        if (goodsCategories.size()==0) {
            model.addAttribute("thirdCategory",parentCategory);
            GoodsCategory secondCategory=categoryService.getCategoryById(parentCategory.getParentId());
            model.addAttribute("secondCategory",secondCategory);
            model.addAttribute("firstCategory",categoryService.getCategoryById(secondCategory.getParentId()));
        }
        else {
            model.addAttribute("categories",goodsCategories);
            if (parentCategory.getCategoryLevel()==2)  {
                model.addAttribute("secondCategory",parentCategory);
                model.addAttribute("firstCategory",categoryService.getCategoryById(parentCategory.getParentId()));
            }
            if (parentCategory.getCategoryLevel()==1)  model.addAttribute("firstCategory",parentCategory);
        }
        return "Admin/AdminCategory";
    }

    @GetMapping("/getDetailCategory/{categoryId}")
    public String getDetailCategory(Model model, @PathVariable("categoryId") Integer categoryId){
        model.addAttribute("detailCategory",categoryService.getCategoryById(categoryId));
        return "Admin/showCategory";
    }
}
