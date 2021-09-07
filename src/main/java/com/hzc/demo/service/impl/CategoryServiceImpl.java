package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.CategoryMapper;
import com.hzc.demo.dao.GoodsMapper;
import com.hzc.demo.pojo.GoodsCategory;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.CategoryMapforMap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* 该业务层下的种类id用categoryId表示
* 该业务层有修改
* */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    GoodsMapper goodsMapper;

    @Override
    public Result saveCategory(GoodsCategory goodsCategory) {
        if (goodsCategory.hasIllegalField()) {
            System.out.println("存在非法字段");
            return new Result(null,1,"存在非法字段");
        }
        if (goodsCategory.getParentId()!=null && categoryMapper.selectById(goodsCategory.getParentId())==null) {
            System.out.println("父节点不存在，不能添加");
            return new Result(null,1,"父节点不存在，不能添加");
        }
        if (categoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(),goodsCategory.getCategoryName())!=null) {
            System.out.println("数据重复");
            return new Result(null,1,"数据重复");
        }
        return categoryMapper.insert(goodsCategory)>0 ?
                new Result(null,0,"添加成功"):new Result(null,1,"添加失败");
    }

    @Override
    public Result updateCateGory(Map<String,Object> map) {
        if (!map.containsKey("categoryLevel") || !map.containsKey("categoryName") || !map.containsKey("categoryId")) {
            System.out.println("传入参数不合法");
            return new Result(null,1,"传入参数不合法");
        }
        Integer categoryLevel= (Integer) map.get("categoryLevel");
        String categoryName= (String) map.get("categoryName");
        Integer categoryId= (Integer) map.get("categoryId");
        GoodsCategory record=categoryMapper.selectById(categoryId);
        if (record==null) {
            System.out.println("数据不存在");
            return new Result(null,1,"数据不存在");
        }
        if (categoryMapper.selectByLevelAndName(categoryLevel,categoryName)!=null) {
            System.out.println("修改后数据重复");
            return new Result(null,1,"修改后数据重复");
        }
        return categoryMapper.updateById(map)>0 ?
                new Result(null,0,"修改成功"):new Result(null,1,"修改失败");
    }

    @Override
    public Result delCateGory(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            System.out.println("参数不合法");
            return new Result(null,1,"不能传递空参");
        }
        int row=0;
        for (Integer id:ids){
            GoodsCategory goodsCategory=categoryMapper.selectById(id);
            if (goodsCategory==null) {
                System.out.println("数据不存在");
                return new Result(null,1,"数据不存在");
            } else if (goodsMapper.selectGoodsByCategoryId(id)!=0) {
                System.out.println("该种类的商品存在无法删除");
                return new Result(null,1,"该种类的商品存在无法删除");
            } else {
                if (categoryMapper.selectByParentId(id).size()!=0) {
                    System.out.println("存在下层分类不能删除");
                    return new Result(null,1,"存在下层分类不能删除");
                }
                row+=categoryMapper.deleteById(id);
            }
        }
        return row>0?new Result(null,0,"删除成功"):new Result(null,1,"删除失败");
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

    //根据种类名字和等级获取当前商品种类
    @Override
    public GoodsCategory getCategoryByLevelAndName(Integer categoryLevel, String categoryName) {
        return categoryMapper.selectByLevelAndName(categoryLevel,categoryName);
    }

    //通过该商品种类的id获取其所有子节点的种类
    @Override
    public List<GoodsCategory> getCurrentCategories(Integer categoryId) {
        return categoryMapper.selectByParentId(categoryId);
    }

    //根据等级获取当前种类等级的商品种类集合
    @Override
    public List<GoodsCategory> getCategoriesByLevel(Integer categoryLevel) {
        return categoryMapper.selectByLevel(categoryLevel);
    }

    //根据种类id获取当前id的种类
    @Override
    public GoodsCategory getCategoryById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }


}
