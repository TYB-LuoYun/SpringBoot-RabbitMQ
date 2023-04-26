package com.example.springboot2rabbitmq.controller;

import com.example.springboot2rabbitmq.fanout.FanoutSender;
import com.example.springboot2rabbitmq.hello.HelloSender;
import com.example.springboot2rabbitmq.hello.HelloSender2;
import com.example.springboot2rabbitmq.model.User;
import com.example.springboot2rabbitmq.object.ObjectSender;
import com.example.springboot2rabbitmq.topic.TopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloSender helloSender;
    @Autowired
    private HelloSender2 helloSender2;
    @Autowired
    private ObjectSender objectSender;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private FanoutSender fanoutSender;

    @RequestMapping("hello")
    public String hello(){
        helloSender.send();
        return "ok";
    }

    @RequestMapping("oneToMany")
    public String oneToMany(){
        helloSender.oneToMany();
        return "ok";
    }

    @RequestMapping("manyToMany")
    public String manyToMany(){

        for (int i = 0; i < 10; i++) {
            helloSender.send();
            helloSender2.send();
        }
        return "ok";
    }

    @RequestMapping("sendObject")
    public String sendObject(){

        User user =new User();
        objectSender.send(user);
        return "ok";
    }

    @RequestMapping("sendTopic")
    public String sendTopic(){

        topicSender.send();
        topicSender.send1();
        topicSender.send2();
        return "ok";
    }
    @RequestMapping("sendFanout")
    public String sendFanout(){

        fanoutSender.send();
        return "ok";
    }

}
