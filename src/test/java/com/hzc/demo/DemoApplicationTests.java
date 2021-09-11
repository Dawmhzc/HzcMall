package com.hzc.demo;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Order;
import com.hzc.demo.service.OrderService;
import com.hzc.demo.service.TimeCartService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class DemoApplicationTests {
    @Resource
    private TimeCartService timeCartService;

    @Test
    void contextLoads() {
    }
}
