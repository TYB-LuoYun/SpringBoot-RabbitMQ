package com.example.springboot2rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayRabbitConfig {

    public static final String DELAY_EXCHANGE_NAME = "delay.queue.demo.business.exchange";
    public static final String DELAY_QUEUEA_NAME = "delay.queue.demo.business.queuea";
    public static final String DELAY_QUEUEB_NAME = "delay.queue.demo.business.queueb";
    public static final String DELAY_QUEUEA_ROUTING_KEY = "delay.queue.demo.business.queuea.routingkey";
    public static final String DELAY_QUEUEB_ROUTING_KEY = "delay.queue.demo.business.queueb.routingkey";
    public static final String DEAD_LETTER_EXCHANGE = "delay.queue.demo.deadletter.exchange";

    public static final String DEAD_LETTER_QUEUEA_NAME = "delay.queue.demo.deadletter.queuea";
    public static final String DEAD_LETTER_QUEUEB_NAME = "delay.queue.demo.deadletter.queueb";




    public static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "delay.queue.demo.deadletter.delay_10s.routingkey";
    public static final String DEAD_LETTER_QUEUEB_ROUTING_KEY = "delay.queue.demo.deadletter.delay_20s.routingkey";

    // 声明延时Exchange
    @Bean("delayExchange")
    public DirectExchange delayExchange(){
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 声明延时队列A 延时10s
    // 并绑定到对应的死信交换机
    @Bean("delayQueueA")
    public Queue delayQueueA(){
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(DELAY_QUEUEA_NAME).withArguments(args).build();
    }

    // 声明延时队列B 延时 20s
    // 并绑定到对应的死信交换机
    @Bean("delayQueueB")
    public Queue delayQueueB(){
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEB_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 20000);
        return QueueBuilder.durable(DELAY_QUEUEB_NAME).withArguments(args).build();
    }

    // 声明死信队列A 用于接收延时10s处理的消息
    // =====把该队列 与 deadLetterExchange交换机绑定并指定路由，其中deadLetterExchange这个交换机被指定为死信交换机被绑定到了延时队列delayQueueA
    // ===== 所以当发消息给 死信交换机的时候  会进入到延时队列
    @Bean("deadLetterQueueAA")
    public Queue deadLetterQueueA(){
        return new Queue(DEAD_LETTER_QUEUEA_NAME);
    }

    // 声明死信队列B 用于接收延时60s处理的消息
    @Bean("deadLetterQueueBB")
    public Queue deadLetterQueueB(){
        return new Queue(DEAD_LETTER_QUEUEB_NAME);
    }

    // 声明延时队列A绑定关系
    @Bean
    public Binding delayBindingA(@Qualifier("delayQueueA") Queue queue,
                                 @Qualifier("delayExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEA_ROUTING_KEY);
    }

    // 声明业务队列B绑定关系
    @Bean
    public Binding delayBindingB(@Qualifier("delayQueueB") Queue queue,
                                 @Qualifier("delayExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEB_ROUTING_KEY);
    }

    // 声明死信队列A绑定关系
    @Bean
    public Binding deadLetterBindingA(@Qualifier("deadLetterQueueAA") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEA_ROUTING_KEY);
    }

    // 声明死信队列B绑定关系
    @Bean
    public Binding deadLetterBindingB(@Qualifier("deadLetterQueueBB") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEB_ROUTING_KEY);
    }


    @Bean
    public CustomExchange customExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("DELAYED_EXCHANGE_NAME", "x-delayed-message", true, false, args);
    }
}
