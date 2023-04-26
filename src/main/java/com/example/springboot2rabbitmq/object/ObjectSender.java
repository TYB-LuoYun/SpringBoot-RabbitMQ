package com.example.springboot2rabbitmq.object;

import com.example.springboot2rabbitmq.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectSender {

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    public void send (User user){
        System.out.println("user:"+user.toString());
        this.rabbitmqTemplate.convertAndSend("object",user);
    }
}
