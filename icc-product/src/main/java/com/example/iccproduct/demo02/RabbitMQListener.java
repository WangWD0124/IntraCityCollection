package com.example.iccproduct.demo02;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitMQListener {

    //direct
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "icc.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenFanoutQueue1(String msg) throws InterruptedException{
        System.out.println("消费者1接收到消息：【"+msg+"】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "icc.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenFanoutQueue2(String msg) throws InterruptedException{
        System.out.println("消费者2接收到消息：【"+msg+"】");
    }

    // topic
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "icc.topic", type = ExchangeTypes.TOPIC),
            key = "China.#"
    ))
    public void listenTopicQueue1(String msg){
        System.out.println("消费者3接收到topic.queue1的消息：【" + msg + "】");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "icc.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg){
        System.out.println("消费者4接收到topic.queue2的消息：【" + msg + "】");
    }

    //消息转换器
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "object.queue"),
            exchange = @Exchange(name = "icc.object", type = ExchangeTypes.FANOUT),
            key = ""
    ))
    public void listenJsonexQueue(Map<String, Object> map){
        System.out.println("消费者收到jsonex.queue的消息：【"+map+"】");
    }

}
