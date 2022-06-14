package com.hzc.demo.service.impl;

import com.hzc.demo.commom.Result;
import com.hzc.demo.dao.TimeCartMapper;
import com.hzc.demo.dao.UserMapper;
import com.hzc.demo.pojo.TimeCart;
import com.hzc.demo.pojo.User;
import com.hzc.demo.service.TimeCartService;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.hzc.demo.commom.ErrorEnum.*;

@EnableScheduling
@Service
public class TimeCartServiceImpl implements TimeCartService {
    @Resource
    private TimeCartMapper timeCartMapper;
    @Resource
    JavaMailSenderImpl mailSender;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result addTimeCart(TimeCart timeCart) {
        if (timeCart == null || timeCart.getGoodsId() == null || timeCart.getGoodsNumb() == null || timeCart.getGoTime().isEmpty())
            return Result.fail(INVALID_PARAMS);
        if (timeCart.getUserId() == null)
            return Result.fail(LOGIN_STATE);
        if (timeCart.getGoodsNumb() <= 0)
            return Result.fail(INVALID_PARAMS);
        return Result.OK(timeCartMapper.addTimeCart(timeCart));
    }

    @Override
    public Result delTimeCart(int id) {
        if (timeCartMapper.delTimeCart(id) == 0) return Result.fail(DATE_NOEXIT);
        return Result.OK();
    }

    @Override
    public Result updTimeCart(TimeCart timeCart) {
        if (timeCart.getUserId() == null || timeCart.getGoodsId() == null || timeCart.getGoodsNumb() <= 0 ||
                timeCart.getGoodsNumb() == null || timeCart.getGoTime().isEmpty())
            return Result.fail(INVALID_PARAMS);
        if (timeCart.getAddress().isEmpty() || timeCart.getMobile().isEmpty())
            return Result.fail(ADDRESS_INPUT);
        return Result.OK(timeCartMapper.updTimeCart(timeCart));
    }

    @Override
    public Result listTimeCart(int id) {
        List<TimeCart> cartList = timeCartMapper.listTimeCart(id);
        if (cartList == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(cartList);
    }

    @Override
    @Scheduled(cron = "0/5 * * * * ?")
    public Result weakBuy() throws MessagingException {
        List<TimeCart> list = timeCartMapper.weakOrMouth("按周购买",1);
        Set<Integer> userIds = new HashSet<>();
        for (TimeCart timeCart:list) {
            timeCart.setState(3);
            timeCartMapper.updTimeCart(timeCart);
            userIds.add(timeCart.getUserId());
            System.out.println(userIds);
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            Iterator<Integer> iterator=userIds.iterator();
            while (iterator.hasNext()){
                User user=userMapper.getUser(iterator.next());
                String QQMail=user.getUserEmail();
                String text="邮件正文自定义";
                MimeMessage mimeMessage = mailSender.createMimeMessage();//返回类型为SmartMimeMessage
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject("通知-您定时购买的商品要支付了");
                helper.setText(text,true);
                helper.setTo(QQMail);//接受邮件的邮箱
                helper.setFrom(mailSender.getUsername());//发送邮件的邮箱
                mailSender.send(mimeMessage);
            }
        }
        return Result.OK();
    }

    @Override
    @Scheduled(cron = "0/5 * * * * ?")
    public Result mouthBuy() throws MessagingException {
        List<TimeCart> cartList = timeCartMapper.weakOrMouth("按月购买",2);
        Set<Integer> userIds = new HashSet<>();
        for (TimeCart timeCart:cartList) {
            timeCart.setState(3);
            timeCartMapper.updTimeCart(timeCart);
            userIds.add(timeCart.getUserId());
            System.out.println(userIds);
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            Iterator<Integer> iterator=userIds.iterator();
            while (iterator.hasNext()){
                User user=userMapper.getUser(iterator.next());
                String QQMail=user.getUserEmail();
                String text="邮件正文自定义";
                MimeMessage mimeMessage = mailSender.createMimeMessage();//返回类型为SmartMimeMessage
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject("通知-您定时购买的商品要支付了");
                helper.setText(text,true);
                helper.setTo(QQMail);//接受邮件的邮箱
                helper.setFrom(mailSender.getUsername());//发送邮件的邮箱
                mailSender.send(mimeMessage);
            }
        }
        return Result.OK();
    }

    @Override
    public Result getTimeCart(int id) {
        TimeCart timeCart = timeCartMapper.getTimeCart(id);
        if (timeCart == null) return Result.fail(DATE_NOEXIT);
        return Result.OK(timeCart);
    }

}
