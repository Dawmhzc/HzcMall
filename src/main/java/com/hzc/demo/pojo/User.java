package com.hzc.demo.pojo;

import lombok.Data;

@Data
public class User {
    private int userId;
    public String userName;
    private String userPwd;
    private String userMobile;
    public String userEmail;
}
