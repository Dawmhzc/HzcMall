package com.hzc.demo.service;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.TimeCart;

import javax.mail.MessagingException;


public interface TimeCartService {
    Result addTimeCart(TimeCart timeCart);
    Result delTimeCart(int id);
    Result updTimeCart(TimeCart timeCart);
    Result listTimeCart(int id);
    Result weakBuy() throws MessagingException;
    Result mouthBuy() throws MessagingException;
    Result getTimeCart(int id);
}
