package com.ys.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void adminTest() throws Exception {

        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));

        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));

        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));

        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));

        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        rabbitAdmin.declareBinding(new Binding("test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct", "direct", new HashMap<>()));

        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("test.topic.queue", false))
                        .to(new TopicExchange("test.topic.queue", false, false))
                        .with("user.#")
        );

        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("test.fanout.queue", false))
                        .to(new FanoutExchange("test.fanout.queue", false, false))
        );

        //清空队列数据
        rabbitAdmin.purgeQueue("test.topic.queue", false);
    }
}
