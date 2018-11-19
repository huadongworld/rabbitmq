package com.ys.api.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author HD
 * @date 2018/11/12 8:58
 */
public class ProducerDirectExchange {

    public static void main(String[] args) throws Exception {

        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.1.141");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2 创建Connection
        Connection connection = connectionFactory.newConnection();
        //3 创建Channel
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";

        //5 发送
        String msg = "Hello World RabbitMQ Direct Exchange Message ... ";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //6 关闭资源
        channel.close();
        connection.close();
    }
}
