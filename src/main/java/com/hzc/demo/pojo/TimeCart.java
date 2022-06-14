package com.hzc.demo.pojo;

import lombok.Data;

@Data
public class TimeCart {
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private Integer goodsNumb;
    private String address;
    private String mobile;
    private String goTime;
    private Integer state;
}
