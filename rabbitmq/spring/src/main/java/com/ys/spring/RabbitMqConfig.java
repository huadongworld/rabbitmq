package com.ys.spring;

import com.rabbitmq.client.Channel;
import com.ys.spring.adapter.MessageDelegate;
import com.ys.spring.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HuaDong
 * @date 2018/11/19 20:37
 */
@Configuration
@ComponentScan({"com.ys.spring.*"})
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("192.168.1.140:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //表示spring会去加载，不写不会加载
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     *  FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     *  HeadersExchange：通过添加属性key-value匹配
     *  DirectExchange: 按照routingkey分发到指定队列
     *  TopicExchange: 多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true);
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true);
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true);
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //监控队列
        container.setQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID().toString());

//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                System.err.println("----------消费者: " + msg);
//            }
//        });

//        //lambda表达式写法，貌似有些问题
//        container.setMessageListener((Message message, Channel channel) -> {
//            String msg = new String(message.getBody());
//            System.out.println(msg);
//        });

//         1.适配器方式. 默认是有自己的方法名字的：handleMessage
         MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//         可以自己指定一个方法的名字: consumeMessage
         adapter.setDefaultListenerMethod("consumeMessage");
//         也可以添加一个转换器: 从字节数组转换为String
         adapter.setMessageConverter(new TextMessageConverter());
         container.setMessageListener(adapter);

//         2 适配器方式: 我们的队列名称 和 方法名称 也可以进行一一的匹配
//         MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//         adapter.setMessageConverter(new TextMessageConverter());
//         Map<String, String> queueOrTagToMethodName = new HashMap<>();
//         queueOrTagToMethodName.put("queue001", "method1");
//         queueOrTagToMethodName.put("queue002", "method2");
//         adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//         container.setMessageListener(adapter);

        return container;
    }

}
