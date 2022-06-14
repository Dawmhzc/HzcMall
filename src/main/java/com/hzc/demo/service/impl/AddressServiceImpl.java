package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.AddressMapper;
import com.hzc.demo.pojo.UserAddress;
import com.hzc.demo.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.hzc.demo.commom.ErrorEnum.*;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressMapper addressMapper;

    @Override
    public Result addAddress(UserAddress userAddress) {
        if (userAddress == null) return Result.fail(INVALID_PARAMS);
        if (userAddress.getAddress().isEmpty() || userAddress.getMobile().isEmpty()) return Result.fail(ADDRESS_INPUT);
        if (userAddress.getUserId() == null) return Result.fail(LOGIN_STATE);
        return Result.OK(addressMapper.addAddress(userAddress));
    }

    @Override
    public Result deleteAddress(int id) {
        if (addressMapper.deleteAddress(id) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result listAddress(int userId) {
        List<UserAddress> userAddressList = addressMapper.listAddress(userId);
        if (userAddressList == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(userAddressList);
    }

    @Override
    public Result getAddress(int id) {
       UserAddress userAddress = addressMapper.getAddress(id);
       if (userAddress == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(userAddress);
    }
}
