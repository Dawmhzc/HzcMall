package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.PriceSurge;

public interface PriceSurgeService {

    PriceSurge getSurge(Integer goodsId);
    Result insertSurge(Integer goodsId, String firstSurge);
    Result updateSurge(String newSurge, Integer goodsId);
    Result deleteSurge(Integer goodsId);
}
