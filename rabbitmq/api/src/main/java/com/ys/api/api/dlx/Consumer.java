package com.ys.api.api.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HD
 * @date 2018/11/18 12:58
 */
public class Consumer {

	/**
	 * 哪些消息会进入死信队列？
	 *
	 * 1.消息被拒绝（basic.reject/basic.nack）并且requeue=false
	 * 2.消息TTL（设置的生命周期）过期
	 * 3.队列达到最大高度
	 *
	 */
	public static void main(String[] args) throws Exception {

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.1.140");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		// 这就是一个普通的交换机 和 队列 以及路由
		String exchangeName = "test_dlx_exchange";
		String routingKey = "dlx.#";
		String queueName = "test_dlx_queue";

		//正常队列声明：
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		
		Map<String, Object> arguments = new HashMap<>(10);
		//设置死信队列exchange
		arguments.put("x-dead-letter-exchange", "dlx.exchange");
		//这个agruments属性，要设置到声明队列上
		channel.queueDeclare(queueName, true, false, false, arguments);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		//死信队列的声明:
		channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
		channel.queueDeclare("dlx.queue", true, false, false, null);
		channel.queueBind("dlx.queue", "dlx.exchange", "#");
		
		channel.basicConsume(queueName, true, new MyConsumer(channel));
	}
}
