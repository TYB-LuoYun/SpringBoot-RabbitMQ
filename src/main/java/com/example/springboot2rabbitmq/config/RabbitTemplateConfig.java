package com.example.springboot2rabbitmq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTemplateConfig {

    @Autowired

    CachingConnectionFactory cachingConnectionFactory;

    @Bean
    RabbitTemplate rabbitTemplate() {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                System.out.println(msgId + ": 消息发送成功");
            } else {
                System.out.println(msgId + ": 消息发送失败");
            }
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println(message);
            System.out.println(replyCode);
            System.out.println(replyText);
            System.out.println(exchange);
            System.out.println(routingKey);
        });

        return rabbitTemplate;
    }
}

// 消息只要被 rabbitmq broker 接收到就会执行 confirmCallback
// 如果是 cluster 模式，需要所有 broker 接收到才会调用 confirmCallback
// 被 broker 接收到只能表示 message 已经到达服务器，并不能保证消息一定会被投递到目标 queue 里

// confirm 模式只能保证消息到达 broker，不能保证消息准确投递到目标 queue 里。
// 在有些业务场景下，我们需要保证消息一定要投递到目标 queue 里，此时就需要用到 return 退回模式
// 这样如果未能投递到目标 queue 里将调用 returnCallback，可以记录下详细到投递数据，定期的巡检或者自动纠错都需要这些数据
