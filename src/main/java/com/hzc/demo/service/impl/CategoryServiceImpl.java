package com.hzc.demo.service.impl;

import com.hzc.demo.dao.CategoryMapper;
import com.hzc.demo.dao.GoodsMapper;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.CategoryMapforMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* 该业务层下的种类id用categoryId表示
* */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    GoodsMapper goodsMapper;

    @Override
    public int saveCategory(GoodsCategory goodsCategory) {
        if (goodsCategory.hasIllegalField()) {
            System.out.println("存在非法字段");
            return 0;
        }
        if (goodsCategory.getParentId()!=null && categoryMapper.selectById(goodsCategory.getParentId())==null) {
            System.out.println("父节点不存在，不能添加");
        }
        if (categoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(),goodsCategory.getCategoryName())!=null) {
            System.out.println("数据重复");
        }
        return categoryMapper.insert(goodsCategory)>0 ? 1:0;
    }

    @Override
    public int updateCateGory(Map<String,Object> map) {
        if (!map.containsKey("categoryLevel") || !map.containsKey("categoryName") || !map.containsKey("categoryId")) {
            System.out.println("传入参数不合法");
            return 0;
        }
        Integer categoryLevel= (Integer) map.get("categoryLevel");
        String categoryName= (String) map.get("categoryName");
        Integer categoryId= (Integer) map.get("categoryId");
        GoodsCategory record=categoryMapper.selectById(categoryId);
        if (record==null) {
            System.out.println("数据不存在");
            return 0;
        }
        if (categoryMapper.selectByLevelAndName(categoryLevel,categoryName)!=null) {
            System.out.println("修改后数据重复");
            return 0;
        }
        return categoryMapper.updateById(map)>0 ? 1:0;
    }

    @Override
    public int delCateGory(Integer id) {
        if (id==null) {
            System.out.println("参数不合法");
            return 0;
        }
        GoodsCategory goodsCategory=categoryMapper.selectById(id);
        if (goodsCategory==null) {
            System.out.println("数据不存在");
            return 0;
        } else if (goodsMapper.selectGoodsByCategoryId(id)!=0) {
            System.out.println("该种类的商品存在无法删除");
            return 0;
        } else {
            if (categoryMapper.selectByParentId(id).size()!=0) {
                System.out.println("存在下层分类不能删除");
                return 0;
            }
            return categoryMapper.deleteById(id)>0 ? 1:0;
        }
    }

    //获取所有商品种类
    @Override
    public List<CategoryMapforMap> getCategoryList() {
        List<GoodsCategory> firstLevels=categoryMapper.selectByLevel(1);
        if (firstLevels.size()>0) {
            List<GoodsCategory> secondLevels=categoryMapper.selectByLevel(2);
            if (secondLevels.size()>0) {
                List<GoodsCategory> thirdLevels=categoryMapper.selectByLevel(3);
                if (thirdLevels.size()>0) {
                    List<CategoryMap> secondMaps=new ArrayList<>();
                    //根据第二层种类的id获取对应的第三层种类
                    for (GoodsCategory second : secondLevels) {
                        CategoryMap secondMap=new CategoryMap();
                        List<GoodsCategory> thirdList=categoryMapper.selectByParentId(second.getCategoryId());
                        secondMap.copy(second);
                        secondMap.setGoodsCategoryList(thirdList);
                        secondMaps.add(secondMap);
                    }
                    List<CategoryMapforMap> firstMaps=new ArrayList<>();
                    //根据第一层种类的id获取对应的第二层种类
                    for (GoodsCategory first : firstLevels) {
                        CategoryMapforMap firstMap=new CategoryMapforMap();
                        int index;
                        firstMap.copy(first);
                        for (index=secondMaps.size()-1; index>=0; index--) {
                            if (secondMaps.get(index).getParentId().equals(first.getCategoryId())) {
                                firstMap.getGoodsCategoryMapList().add(secondMaps.get(index));
                                secondMaps.remove(index);
                            }
                        }
                        /*for (CategoryMap secondMap : secondMaps) {
                            if (secondMap.getParentId().equals(first.getCategoryId())) {
                                firstMap.getGoodsCategoryMapList().add(secondMap);
                            }
                        }*/
                        firstMaps.add(firstMap);
                    }
                    return firstMaps;
                }
            }
        }
        return null;
    }

    //根据商品种类id查找父节点及同父节点的商品种类
    @Override
    public CategoryMap getCategoriesForSearch(Integer categoryId) {
        CategoryMap categoryMap=new CategoryMap();
        GoodsCategory currentGoodsCategory=categoryMapper.selectById(categoryId);
        if (currentGoodsCategory!=null && currentGoodsCategory.getCategoryLevel()==3) {
            GoodsCategory secondGoodCategory=categoryMapper.selectById(currentGoodsCategory.getParentId());
            if (secondGoodCategory!=null && secondGoodCategory.getCategoryLevel()==2) {
                List<GoodsCategory> goodsCategoryList=categoryMapper.selectByParentId(secondGoodCategory.getCategoryId());
                categoryMap.copy(secondGoodCategory);
                categoryMap.setGoodsCategoryList(goodsCategoryList);
                return categoryMap;
            }
            return null;
        } else if (currentGoodsCategory!=null && currentGoodsCategory.getCategoryLevel()==2) {
            GoodsCategory firstGoodCategory=categoryMapper.selectById(currentGoodsCategory.getParentId());
            if (firstGoodCategory!=null && firstGoodCategory.getCategoryLevel()==1) {
                List<GoodsCategory> goodsCategoryList=categoryMapper.selectByParentId(firstGoodCategory.getCategoryId());
                categoryMap.copy(firstGoodCategory);
                categoryMap.setGoodsCategoryList(goodsCategoryList);
                return categoryMap;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public GoodsCategory getCategoryByLevelAndName(Integer categoryLevel, String categoryName) {
        return categoryMapper.selectByLevelAndName(categoryLevel,categoryName);
    }

    //通过该商品种类的id获取其所有子节点的种类
    @Override
    public List<GoodsCategory> getCurrentCategories(Integer categoryId) {
        return categoryMapper.selectByParentId(categoryId);
    }

    @Override
    public List<GoodsCategory> getCategoriesByLevel(Integer categoryLevel) {
        return categoryMapper.selectByLevel(categoryLevel);
    }


}
