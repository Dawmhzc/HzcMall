package com.hzc.demo.comfig;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 德鲁伊配置
 * @author hzc
 * @date 2021/8/2
 */
public class DruidComfig {
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "root");
        initParams.put("loginPassword", "123456");
        initParams.put("allow", "localhost");
        initParams.put("deny", "");
        bean.setInitParameters(initParams);
        return bean;
    }
}
