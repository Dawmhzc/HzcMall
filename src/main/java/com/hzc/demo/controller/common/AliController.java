package com.hzc.demo.controller.common;

import com.alipay.api.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class AliController {
    private final String APP_ID = "2021000117696312";
    private final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCUrPA6/ZuwMmcMNBS2ITRhHINdh2L4usMfv6wJejwr0UlQgcm8XNXa+a4Ics1aOFyE1h6qkzvz3YWKL2fyy86K/bAhvS7a8RqqGD7Hzb70KiZ3oNc/BC7HptvDiFEgXkcGZQybVqDcTIuy2+R+mTlV5QfV9u/q8eGxTcJ2JyyDuPrEcukeTooLEP0l87YUV8O4DiJ1R/qGECocZ24eHviMHF+HGhceJqj4tQVGBhAqLHhoInIGXVnR4VwJxgchzGkwzJ4IYD8QgFnEFpYtxzGbbRDUe5TWb/DkME3xXBnUuu3MPCZbsEopMMtGHJT9Zr6lonx9U/ThFvJznAzDvOFPAgMBAAECggEBAJL6iJ942jINEexxjEwldVfPzVpoHoLQd1fqr9PKwLN8uiLZvCr4B/7ruI3nmHVppKU53KU62xkR1ROwG+5eZ3jmpPolwa12j6q6P05dazqMdOzs1/4XT3vWzAl4uOLr8QE0Yosqz+oZzj1qv3AcYB83RVDUR4iXyUv0GJufzJUGgiw7z0lSjOzbhCG2ORWxKX5mso8sf+tYukDip8GS8yKq/RN5l8txI3CbYovzvCf8mbQo9b9W4vlWdg91C4JWmri+bQbOlD+1vPq7u1neYHYI0FKcIJ6opYZ7/U0BxlJabmGvxQ57bzljBdi+FBdIPriiCj3JqyfE2qWT7mwK6UECgYEA5WINj12QmoV9PXicJtXbhBiD6vyQUfKoSEeVp2f7g0KaRD6E9MtNIjys3wCvP7BNR/X60KJCkka+FkFxV4MZ5+ZuyUfIamlpEr/RSEaswnaXNcG5wu8sHUqe3K+a+TwbT+jXgQZ7h25jI6rNyvKAHeG4Kj22aL3i3cgn/szu8i8CgYEApe1tDbW37sHwcSoD/556Giyz7z4VglX5/I8ytu7eSb3TZLtwnX44JPHwoHqZ80SsMvfAzW9S4e/t8cZHmHS46OtnlnOReAiKpfnjS9V3PZ7qz92hWOtzx1aosE25/G32YVOOyY6n6APHWPgUklT6cphQVQnt++fnCG5PDwLi2uECgYBLpYuNRTEEFJPZ6uWsVQjAxojyvPonXFwtAhaTj9I9VoCjku6sAEPez52/KW2iFlVWLwPZPDmyHcxgcJKdZqnrdW4ByT+KAFtbiJDPQieFNjDl1I6bf97PhiVf6DW2jiD/uG0hqu6gGkJtp82YaWLg9+iRXarDXiPsOi8ec501lwKBgQClcaFlJCXsGNBn5Z7rB9dztdML33mO5a9HVnkY0gAuwum+7O6S+sF8hQiK2vQaDgUtJL/HlxKrWZKFmlDVAL1G8UHuT/liGBI3AHzUplooxQYYkUJJ6SsePZghgr7JpdMepDQhYO6mdu4UFfhdGAJItPqUcDwtCsPgMiz+KMCNwQKBgEtwOiA9Ocx8CSyKVKA2Jn6NISRwkM06wnR0Ow49ZtC7EBhzuOkUhpf8QWTrGJlVHi0UcVGmB/ZXwqWgU+vOA4IPYtSwyU5N1UdKM9vjKKDFHfDoPiqtAoMORFFWHD5e4Ubn+ac9dTz78u54hTCeziUUWT3cerzc/jPU5maWpnU9";
    private final String CHARSET = "UTF-8";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwShnp8jzHXUITwCCOGrYxtwfOOQrlD9eWpo010MxF7vhdL0dLjw1h+LBlhNK5EajBr2U9PYTSwRv12Qs/ARonP7Qu8T+ddXBDa7/2KkxcsKqF+QGwhmMHI+simySHV5oyowEaduw7cvqxK7pnbQ1rjHRIPcEortzplOwkY2moI05pQt5GOhY6I65jWb2i47qWbS85R/B1xcboZDKyTWER3PVMCI8OjpnuokFnBKt69zwuFBTwZHrDgxTqfDb+Lx5DxgzoXO4xMb+GCL/yorRQDowVtxtJ/dtdTG+x9BFOg7FSWU2sOA9GXtTs4+1CxJUgz1VzAQmzxqHQRjKpQ76HwIDAQAB";
    //沙箱接口路径
    private final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后异步调用方法,公网地址
    private final String NOTIFY_URL = "";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面
    private final String RETURN_URL = "";

    /**
     * 调用阿里支付接口
     * @param response
     */
    @RequestMapping("/alipay")
    public void aliPay(HttpServletResponse response) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest aliRequest = new AlipayTradePagePayRequest();
        aliRequest.setReturnUrl("回调地址");
        aliRequest.setNotifyUrl("通知地址");
        aliRequest.setBizContent("订单信息");
        String form = "";
        try {
            form = alipayClient.pageExecute(aliRequest).getBody();
            response.setContentType("text/html;charset=" + CHARSET);
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/returnurl")
    public String returnUrl(HttpServletRequest request) throws Exception {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        Iterator<String> iter = requestParams.keySet().iterator();
        if (iter.hasNext()){
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i=0;i<values.length;i++)
                valueStr = (i == values.length-1)?valueStr + values[i]:valueStr + values[i] + ",";
            valueStr = new String(valueStr.getBytes("UTF-8"),"UTF-8");
            params.put(name,valueStr);
        }

        boolean signsuccess = AlipaySignature.certVerifyV1(params,ALIPAY_PUBLIC_KEY,CHARSET,SIGN_TYPE);
        if (signsuccess){
            return "OK";
        }else {
            return "fail";
        }
    }
}
