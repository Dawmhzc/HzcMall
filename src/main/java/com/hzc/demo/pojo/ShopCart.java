package com.hzc.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCart {
    private Integer shopCartId;//购物车ID

    private int userId;//用户ID

    private Integer goodsId;//商品ID

    private Integer goodsCount;//购买数量

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;//创建时间

    public boolean hasIllegalField() {
        return (goodsId==null || goodsId.equals(0)) || (goodsCount==null || goodsCount<0) || (userId==0);
    }
}
