package com.ys.springboot.producer;

import com.ys.springboot.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author HuaDong
 * @date 2018/11/26 21:08
 */
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息方法调用: 构建Message消息
     *
     * @param message
     * @param properties
     * @throws Exception
     */
    public void send(Object message, Map<String, Object> properties) throws Exception {

        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);
        //消息确认模式
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //消息返回模式
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("1234567890");
        rabbitTemplate.convertAndSend("exchange-1", "springboot.abc", msg, correlationData);
    }

    /**
     * 回调函数: confirm确认
     */
    private final RabbitTemplate.ConfirmCallback confirmCallback =
            (CorrelationData correlationData, boolean ack, String cause) -> {
                System.err.println("correlationData: " + correlationData);
                System.err.println("ack: " + ack);
                //如果不成功做一些异常处理或者补偿机制
                if (!ack) {
                    System.err.println("异常处理....");
                }
            };


    /**
     * 回调函数: return返回
     */
    private final RabbitTemplate.ReturnCallback returnCallback =
            (org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
                System.err.println("return exchange: " + exchange + ", routingKey: "
                        + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
            };


    /**
     * 发送消息方法调用: 构建自定义对象消息
     *
     * @param order
     * @throws Exception
     */
    public void sendOrder(Order order) throws Exception {

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("0987654321");
        rabbitTemplate.convertAndSend("exchange-2", "springboot.def", order, correlationData);
    }
}
