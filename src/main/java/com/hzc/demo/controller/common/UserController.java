package com.hzc.demo.controller.common;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.User;
import com.hzc.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userServiceImpl;

    @RequestMapping("/admin/all")
    public String listUser(Map<String,Object> map){
        List<User> userList = (List<User>) userServiceImpl.listUser().getData();
        map.put("userList",userList);
        return "Admin/adminUserList";
    }

    @RequestMapping("/admin/upd")
    public String updUser(User user,Map<String,Object> map){
        Result result = userServiceImpl.updateUser(user);
        if (result.getCode() != 0){
            map.put("message",result.getMessage());
            return "Admin/auth-404";
        }
        return "redirect:/user/admin/all";
    }

    @RequestMapping("/admin/delete")
    public String delUser(int userId){
        userServiceImpl.deleteUser(userId);
        return "redirect:/user/admin/all";
    }

    @RequestMapping("/add")
    public String addUser(@RequestParam("userName") String userName,
                         @RequestParam("userPwd") String userPwd,
                         @RequestParam("userMobile") String userMobile,
                         @RequestParam("userEmail") String userEmail){
        User user = new User();
        user.setUserName(userName);
        user.setUserPwd(userPwd);
        user.setUserMobile(userMobile);
        user.setUserEmail(userEmail);
        user.setUserRight(1);
        userServiceImpl.addUser(user);
        return "redirect:/goods/getGoodsList";
    }

    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam("userName") String userName, @RequestParam("userPwd") String userPwd, Map<String,String> map){
        Result result = userServiceImpl.login(userName,userPwd);
        if (result.getCode() != 0){
            String message = result.getMessage();
            map.put("message",message);
            return "mall/404page";
        }
        User user = (User) result.getData();
        session.setAttribute("userId",user.getUserId());
        session.setAttribute("username",user.getUserName());
        session.setAttribute("userEmail",user.getUserEmail());
        session.setAttribute("userMobile",user.getUserMobile());
        session.setAttribute("rightId",user.getUserRight());
        return "mall/index";
    }

    @RequestMapping("/admin/login")
    public String adminLogin(HttpSession session, Map<String,String> map,
                             @RequestParam("userName") String userName, @RequestParam("userPwd") String userPwd){
        Result result = userServiceImpl.login(userName,userPwd);
        if (result.getCode() != 0) {
            map.put("message",result.getMessage());
            return "Admin/auth-404";
        }
        User user = (User) result.getData();
        int rightId = user.getUserRight();
        if (rightId == 3) return "redirect:/goods/getGoodsList";
        session.setAttribute("userId",user.getUserId());
        session.setAttribute("username",user.getUserName());
        session.setAttribute("userEmail",user.getUserEmail());
        session.setAttribute("userMobile",user.getUserMobile());
        session.setAttribute("rightId",rightId);
        if (rightId == 2) return "redirect:/admin/getAllGoods";
        return "redirect:/user/admin/all";
    }

    @RequestMapping("/exit")
    public String exit(HttpSession session){
        session.removeAttribute("userId");
        session.removeAttribute("username");
        session.removeAttribute("userEmail");
        session.removeAttribute("userMobile");
        session.removeAttribute("rightId");
        session.removeAttribute("address");
        session.removeAttribute("mobile");
        return "redirect:/login";
    }

    @RequestMapping("/admin/exit")
    public String adminExit(HttpSession session){
        session.removeAttribute("userId");
        session.removeAttribute("username");
        session.removeAttribute("userEmail");
        session.removeAttribute("userMobile");
        session.removeAttribute("rightId");
        session.removeAttribute("address");
        session.removeAttribute("mobile");
        return "redirect:/admin/login";
    }
}
