package com.hzc.demo;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Order;
import com.hzc.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {
    @Resource
    private OrderService orderServiceImpl;

    @Test
    void contextLoads() {
        List<Integer> data = (List<Integer>) orderServiceImpl.wishList().getData();
        System.out.println(data);
    }

}
