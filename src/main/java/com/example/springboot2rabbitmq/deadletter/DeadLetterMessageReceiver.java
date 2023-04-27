package com.example.springboot2rabbitmq.deadletter;

import com.example.springboot2rabbitmq.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DeadLetterMessageReceiver {

    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        byte[] body = message.getBody();
        String msg = new String(body);
        System.out.println("收到死信消息A：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Message message,Channel channel) throws IOException {
        byte[] body = message.getBody();
        String msg = new String(body);
        System.out.println("收到死信消息A：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

    }

}
