package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.CategoryMapforMap;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Result saveCategory(GoodsCategory goodsCategory);
    Result updateCateGory(Map<String,Object> map);
    Result delCateGory(List<Integer> ids);
    List<CategoryMapforMap> getCategoryList();
    CategoryMap getCategoriesForSearch(Integer categoryId);
    GoodsCategory getCategoryByLevelAndName(Integer categoryLevel,String categoryName);
    List<GoodsCategory> getCurrentCategories(Integer categoryId);
    List<GoodsCategory> getCategoriesByLevel(Integer categoryLevel);
    GoodsCategory getCategoryById(Integer categoryId);
}
