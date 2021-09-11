package com.hzc.demo.pojo;

import lombok.Data;

@Data
public class User {
    public Integer userId;
    public String userName;
    private String userPwd;
    private String userMobile;
    private String userEmail;
    public Integer userRight;
}
