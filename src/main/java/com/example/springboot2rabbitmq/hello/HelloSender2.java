package com.example.springboot2rabbitmq.hello;

import com.example.springboot2rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender2 {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello " + RabbitmqConfig.simpleDateFormat.format(new Date());
        System.out.println("Sender2 : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

    public void oneToMany() {
        String context = "hello " + RabbitmqConfig.simpleDateFormat.format(new Date());
        System.out.println("Sender2 : " + context);
        for (int i = 0; i < 10; i++) {
            this.rabbitTemplate.convertAndSend("hello", context);
        }
    }
}
