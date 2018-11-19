package com.ys.api.api.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author HD
 * @date 2018/11/15 22:14
 */
public class MyConsumer extends DefaultConsumer {

	private Channel channel ;
	
	public MyConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

		System.err.println("-----------consume message----------");
		System.err.println("consumerTag: " + consumerTag);
		System.err.println("envelope: " + envelope);
		System.err.println("properties: " + properties);
		System.err.println("body: " + new String(body));

		//第二个参数multiple：true批量签收，false不批量签收，对应在消费端设置的basicQos第二个参数，一次处理多个还是一个
		channel.basicAck(envelope.getDeliveryTag(), false);
	}
}
