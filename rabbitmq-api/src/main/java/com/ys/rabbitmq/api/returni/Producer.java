package com.ys.rabbitmq.api.returni;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author HD
 * @date 2018/11/15 21:21
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.11.76");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchange = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyError = "abc.save";
		
		String msg = "Hello RabbitMQ Return Message";

		//java8写法
		channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
			System.err.println("---------handle  return----------");
			System.err.println("replyCode: " + replyCode);
			System.err.println("replyText: " + replyText);
			System.err.println("exchange1: " + exchange1);
			System.err.println("routingKey1: " + routingKey1);
			System.err.println("properties: " + properties);
			System.err.println("body: " + new String(body));
		});

		channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

		//channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
	}
}
