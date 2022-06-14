package com.hzc.demo.service;

import com.hzc.demo.dao.GoodsMapper;
import com.hzc.demo.dao.PriceSurgeMapper;
import com.hzc.demo.dao.ShopCartMapper;
import com.hzc.demo.dao.UserMapper;
import com.hzc.demo.pojo.PriceSurge;
import com.hzc.demo.pojo.ShopCart;
import com.hzc.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

@Service
public class ScheduleService {

    @Autowired
    JavaMailSenderImpl mailSender;
    @Resource
    ShopCartMapper shopCartMapper;
    @Resource
    PriceSurgeMapper priceSurgeMapper;
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    UserMapper userMapper;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void sendQQMail() throws MessagingException {
        System.out.println("每2分钟执行一次");

        MimeMessage mimeMessage = mailSender.createMimeMessage();//返回类型为SmartMimeMessage
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("通知-你有商品降价啦");
        //遍历每个用户
        List<User> users=userMapper.listUser();
        for (User user:users) {
            String QQMail=user.getUserEmail();
            Integer userId=user.getUserId();
            List<ShopCart> shopCartList=shopCartMapper.selectShopCartList(userId);
            List<Double> surgeList= new ArrayList<>();
            String text="";
            //遍历该用户的购物车
            for (ShopCart shopCart:shopCartList) {
                PriceSurge priceSurge=priceSurgeMapper.selectSurge(shopCart.getGoodsId());
                List<String> temp= Arrays.asList(priceSurge.getSurge().split(","));
                for (String surge:temp) {
                    surgeList.add(Double.parseDouble(surge));
                }
                Double nowPrice=goodsMapper.selectById(shopCart.getGoodsId()).getSellingPrice();
                ListIterator listIterator=surgeList.listIterator();
                boolean flag=true;
                while (listIterator.hasPrevious()){
                    Object obj=listIterator.previous();
                    if ((Double)obj < nowPrice) {
                        System.out.println("当前价格不是历史最低价");
                        flag=false;
                    }
                }
                if (flag) {
                    text=text+"<b style='color:red'>商品ID:"+shopCart.getGoodsId()+",商品名:"
                            +goodsMapper.selectById(shopCart.getGoodsId()).getGoodsName()+",当前价格降为:"+nowPrice+"</b><br>";
                }
                surgeList.clear();
            }
            if (text.length()==0) continue;
            helper.setText(text,true);
            helper.setTo(QQMail);
            helper.setFrom(mailSender.getUsername());

            mailSender.send(mimeMessage);
        }

    }
}
