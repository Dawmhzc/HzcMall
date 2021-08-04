package com.hzc.demo.service;

import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.CategoryMapforMap;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    int saveCategory(GoodsCategory goodsCategory);
    int updateCateGory(Map<String,Object> map);
    int delCateGory(Integer id);
    List<CategoryMapforMap> getCategoryList();
    CategoryMap getCategoriesForSearch(Integer categoryId);
    GoodsCategory getCategoryByLevelAndName(Integer categoryLevel,String categoryName);
    List<GoodsCategory> getCurrentCategories(Integer categoryId);
    List<GoodsCategory> getCategoriesByLevel(Integer categoryLevel);
}
