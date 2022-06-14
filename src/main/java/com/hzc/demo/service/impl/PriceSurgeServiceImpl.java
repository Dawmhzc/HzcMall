package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.PriceSurgeMapper;
import com.hzc.demo.pojo.PriceSurge;
import com.hzc.demo.service.PriceSurgeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PriceSurgeServiceImpl implements PriceSurgeService {

    public final int maxsize=50;
    @Resource
    PriceSurgeMapper priceSurgeMapper;

    @Override
    public PriceSurge getSurge(Integer goodsId) {
        PriceSurge priceSurge=priceSurgeMapper.selectSurge(goodsId);
        return priceSurge;
    }

    @Override
    public Result insertSurge(Integer goodsId, String firstSurge) {
        if (firstSurge.length()>maxsize) return new Result(null,1,"价格长度超过限制无法加入数据库");
        return priceSurgeMapper.insertSurge(goodsId,firstSurge)>0 ? new Result(null,0,"插入成功"):
                new Result(null,1,"插入失败");
    }

    @Override
    public Result updateSurge(String newSurge, Integer goodsId) {
        if (newSurge.length()>maxsize) return new Result(null,1,"长度超过限制无法修改记录");
        PriceSurge priceSurge=priceSurgeMapper.selectSurge(goodsId);
        priceSurge.setSurge(priceSurge.getSurge()+","+newSurge);
        while (priceSurge.getSurge().length()>maxsize) {
            priceSurge.setSurge(priceSurge.getSurge().substring(priceSurge.getSurge().indexOf(",")+1));
        }
        return priceSurgeMapper.updateSurge(priceSurge.getSurge(),priceSurge.getGoodsId())>0 ?
                new Result(null,0,"修改成功") : new Result(null,1,"修改失败");
    }

    @Override
    public Result deleteSurge(Integer goodsId) {
        return priceSurgeMapper.deleteSurge(goodsId)>0 ? new Result(null,0,"删除成功") :
                new Result(null,1,"删除失败");
    }
}
