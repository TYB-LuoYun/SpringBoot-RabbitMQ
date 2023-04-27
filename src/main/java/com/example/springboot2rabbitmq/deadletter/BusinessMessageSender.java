package com.example.springboot2rabbitmq.deadletter;

import com.example.springboot2rabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessMessageSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String msg){
        rabbitTemplate.convertAndSend(RabbitConfig.BUSINESS_EXCHANGE_NAME,"",msg);
    }
}
