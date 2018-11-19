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
		connectionFactory.setHost("192.168.1.140");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchange = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyError = "abc.save";
		
		String msg = "Hello RabbitMQ Return Message";

		//设置返回监听 java8写法
		channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
			System.err.println("---------handle  return----------");
			System.err.println("replyCode: " + replyCode);
			System.err.println("replyText: " + replyText);
			System.err.println("exchange1: " + exchange1);
			System.err.println("routingKey1: " + routingKey1);
			System.err.println("properties: " + properties);
			System.err.println("body: " + new String(body));
		});

		// 主要是第三个参数 mandatory，true:表示如果发送消息失败则会返回监听，false:发送消息失败自动删除消息
		channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

		//channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
	}
}
