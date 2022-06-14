package com.hzc.demo.controller.mall;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.UserAddress;
import com.hzc.demo.service.AddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/address")
public class AddressController {
    @Resource
    private AddressService addressServiceImpl;

    @RequestMapping("/all")
    public String listUserAddress(HttpSession session, Map<String,Object> map){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            map.put("message","未登录");
            return "mall/404page";
        }
        Result result = addressServiceImpl.listAddress(userId);
        if (result.getCode() != 0){
            String message = result.getMessage();
            map.put("message",message);
            return "mall/404page";
        }
        List<UserAddress> userAddressList = (List<UserAddress>) result.getData();
        map.put("userAddress",userAddressList);
        return "mall/address";
    }

    @RequestMapping("/set")
    public String setAddress(int id,HttpSession session,Map<String,String> map){
        Result result = addressServiceImpl.getAddress(id);
        if (result.getCode() != 0){
            map.put("message", result.getMessage());
            return "mall/404page";
        }
        UserAddress userAddress = (UserAddress) addressServiceImpl.getAddress(id).getData();
        session.setAttribute("address",userAddress.getAddress());
        session.setAttribute("mobile",userAddress.getMobile());
        return "redirect:/address/all";
    }

    @RequestMapping("/add")
    public String addAddress(@RequestParam("address") String address,HttpSession session,
                             @RequestParam("mobile") String mobile,Map<String,String> map){
        Integer userId = (Integer) session.getAttribute("userId");
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setAddress(address);
        userAddress.setMobile(mobile);
        Result result = addressServiceImpl.addAddress(userAddress);
        if (result.getCode()!=0){
            String message = result.getMessage();
            map.put("message",message);
            return "mall/404page";
        }
        return "redirect:/address/all";
    }

    @RequestMapping("/del")
    public String delAddress(int id, Map<String,String> map){
        Result result = addressServiceImpl.deleteAddress(id);
        if (result.getCode() != 0){
            String message = result.getMessage();
            map.put("message",message);
            return "mall/404page";
        }
        return "redirect:/address/all";
    }
}
