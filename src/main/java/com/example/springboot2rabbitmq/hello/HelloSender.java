package com.example.springboot2rabbitmq.hello;

import com.example.springboot2rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class HelloSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String id = UUID.randomUUID().toString();
        String context = "hello " + RabbitmqConfig.simpleDateFormat.format(new Date());
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context,new CorrelationData(id));
    }

    public void oneToMany() {
        String context = "hello " + RabbitmqConfig.simpleDateFormat.format(new Date());
        System.out.println("Sender : " + context);
        for (int i = 0; i < 10; i++) {
            this.rabbitTemplate.convertAndSend("hello", context);
        }
    }
}
