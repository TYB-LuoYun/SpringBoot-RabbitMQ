package com.example.springboot2rabbitmq.delay;

import com.example.springboot2rabbitmq.config.DelayRabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DelaySender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String message,String type){
        System.out.println("send时间是："+new Date());
        switch (type){
            case "delay_10s":
                rabbitTemplate.convertAndSend(DelayRabbitConfig.DELAY_EXCHANGE_NAME,DelayRabbitConfig.DELAY_QUEUEA_ROUTING_KEY,message);
                break;
            case "delay_20s":
                rabbitTemplate.convertAndSend(DelayRabbitConfig.DELAY_EXCHANGE_NAME,DelayRabbitConfig.DELAY_QUEUEB_ROUTING_KEY,message);
                break;
        }
    }

    public void delayedMessage() {
        String context = "test delay message";
        System.out.println("Send time: " + LocalDateTime.now() + "  Send: " + context);
        //延时时间6秒
        rabbitTemplate.convertAndSend(DelayRabbitConfig.DELAY_EXCHANGE_NAME, DelayRabbitConfig.DELAY_QUEUEB_ROUTING_KEY, context, a -> {
            a.getMessageProperties().setDelay(6000);
            return a;
        });
    }
}
