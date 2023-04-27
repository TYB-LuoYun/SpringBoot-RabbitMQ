package com.example.springboot2rabbitmq.deadletter;


import com.example.springboot2rabbitmq.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BusinessMessageReceiver  {

    @RabbitListener(queues = RabbitConfig.BUSINESS_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        byte[] body = message.getBody();
        String msg = new String(body);
        System.out.println("收到业务消息A：" + new String(message.getBody()));

        boolean ack = true;
        Exception exception = null;
        try {
            if (msg.contains("deadletter")) {
                throw new RuntimeException("dead letter exception");
            }

        } catch (Exception e) {
            ack = false;
            exception = e;
        }

        if (!ack) {
            System.out.println("消费发生异常");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } else {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitListener(queues =RabbitConfig.BUSINESS_QUEUEB_NAME)
    public void receiveB(Message message,Channel channel) throws IOException {
        byte[] body = message.getBody();
        String msg = new String(body);
        System.out.println("收到业务消息B：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);  //这是消费成功
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false); //这是消费失败
        channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);  //表示拒收
    }
}
