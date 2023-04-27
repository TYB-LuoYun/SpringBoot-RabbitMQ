package com.example.springboot2rabbitmq.delay;

import com.example.springboot2rabbitmq.config.DelayRabbitConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class DelayReceiver {

    @RabbitListener(queues = DelayRabbitConfig.DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        System.out.println(message.getBody().toString());
        System.out.println("receiver10时间是:"+new Date());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    @RabbitListener(queues = DelayRabbitConfig.DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Message message,Channel channel) throws IOException {
        System.out.println(message.getBody().toString());
        System.out.println("receiver20时间是:"+new Date());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
