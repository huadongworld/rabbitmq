package com.ys.api.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author HD
 * @date 2018/11/11 20:30
 */
public class Producer {


	public static void main(String[] args) throws Exception {

		//1 创建一个ConnectionFactory, 并进行配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.1.141");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");

		//2 通过连接工厂创建连接
		Connection connection = connectionFactory.newConnection();

		//3 通过connection创建一个Channel
		Channel channel = connection.createChannel();

		//4 通过Channel发送数据
		for (int i = 0; i < 5; i++) {
			String msg = "Hello RabbitMQ!";
			//1 exchange : 不填的话走默认的交换机   2 routingKey
			channel.basicPublish("", "test001", null, msg.getBytes());
		}

		//5 记得要关闭相关的连接
		channel.close();
		connection.close();
	}
}
