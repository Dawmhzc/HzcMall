package com.hzc.demo.util;

import com.hzc.demo.pojo.GoodsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CategoryMap extends GoodsCategory {

    private List<GoodsCategory> goodsCategoryList;

    public CategoryMap() {
        this.goodsCategoryList = new ArrayList<GoodsCategory>();
    }

    public void copy(GoodsCategory goodsCategory) {
        this.setCategoryId(goodsCategory.getCategoryId());
        this.setCategoryName(goodsCategory.getCategoryName());
        this.setCategoryLevel(goodsCategory.getCategoryLevel());
        this.setParentId(goodsCategory.getParentId());
    }
}
