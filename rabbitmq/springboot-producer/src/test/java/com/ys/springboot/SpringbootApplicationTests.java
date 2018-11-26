package com.ys.springboot;

import com.ys.springboot.entity.Order;
import com.ys.springboot.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuaDong
 * @date 2018/11/26 21:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitSender rabbitSender;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void testSender1() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("number", "12345");
        properties.put("send_time", simpleDateFormat.format(new Date()));
        rabbitSender.send("Hello RabbitMQ For Spring Boot!", properties);
    }

    @Test
    public void testSender2() throws Exception {
        Order order = new Order("001", "第一个订单");
        rabbitSender.sendOrder(order);
    }

}
