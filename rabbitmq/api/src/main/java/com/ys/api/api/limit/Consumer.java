package com.ys.api.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author HD
 * @date 2018/11/15 22:14
 */
public class Consumer {

	public static void main(String[] args) throws Exception {

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.1.140");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchangeName = "test_qos_exchange";
		String queueName = "test_qos_queue";
		String routingKey = "qos.#";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		//参数一:消息大小限制，0表示不做限制，参数二：一次处理多少条消息，第三个参数：这个限流策略在哪应用，true:channel级别，false:consumer级别
		channel.basicQos(0, 1, false);

		//1 限流方式 第一件事就是讲第二个参数autoAck设置为 false
		channel.basicConsume(queueName, false, new MyConsumer(channel));
	}
}
