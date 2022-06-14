package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.UserAddress;

public interface AddressService {
    /**
     *
     * 用户添加地址
     * @param userAddress 地址电话
     * @return
     */
    Result addAddress(UserAddress userAddress);

    /**
     * 删除地址
     * @param id 地址Id
     * @return
     */
    Result deleteAddress(int id);

    /**
     * 用户地址展示
     * @param userId 用户Id
     * @return
     */
    Result listAddress(int userId);

    /**
     * 设置为默认地址
     * @param id 地址id
     * @return
     */
    Result getAddress(int id);
}
