package com.hzc.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

/*
 * 暂时考虑将商品名字和商品种类id作为商品重复的参考指标
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {

    private Integer goodsId;
    private String goodsName;
    private String goodsIntro;//商品介绍
    private Integer goodsCategoryId;//商品种类id
    private String goodsImg;//商品图片的url
    //private String goodsCarousel;
    private Double originalPrice;//原价
    private Double sellingPrice;//现价
    //private String tag;
    private Integer sellStatus;//是否下架,1表示下架
    private Integer stockNum;//库存
    private Integer createUser;//商家id？
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer updateUser;//不知道什么作用，数据库暂时未写入该字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//不知道什么作用，数据库暂时未写入该字段

    public boolean hasIllegalField() {
        return !StringUtils.hasLength(goodsName) || goodsCategoryId==null || !StringUtils.hasLength(goodsIntro)
                || createUser==null || stockNum==null || stockNum<0 ;
    }

    public boolean hasIllegalMoney(){
        return (Objects.isNull(originalPrice) || originalPrice<0) && (Objects.isNull(sellingPrice) || sellingPrice<0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return goodsName.equals(goods.goodsName) && goodsCategoryId.equals(goods.goodsCategoryId) && originalPrice.equals(goods.originalPrice) && stockNum.equals(goods.stockNum) && sellingPrice.equals(goods.sellingPrice) && sellStatus.equals(goods.sellStatus) && createUser.equals(goods.createUser);
    }

}
