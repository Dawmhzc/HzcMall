package com.hzc.demo.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    public String  ordId;
    public Integer ordUserId;
    public String ordAddress;
    public String ordBuyTime;
    public BigDecimal ordTotal;
    public Integer ordStatus;
    public Integer ordGoodId;
    public Integer ordGoodNumb;
    public Integer ordShopId;
}
