package com.hzc.demo.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private String  ordId;
    private Integer ordUserId;
    private String ordAddress;
    private String ordMobile;
    private Date ordBuyTime;
    private BigDecimal ordTotal;
    private String ordGoodName;
    private Integer ordGoodNumb;
    private Integer ordShopId;
}
