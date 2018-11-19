package com.ys.api.api.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author HD
 * @date 2018/11/15 22:11
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.1.140");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchange = "test_consumer_exchange";
		String routingKey = "consumer.save";
		
		String msg = "Hello RabbitMQ Consumer Message";

		for (int i = 0; i < 5; i++) {
			channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
		}
	}
}
