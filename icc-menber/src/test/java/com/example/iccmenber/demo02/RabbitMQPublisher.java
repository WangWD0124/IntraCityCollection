package com.example.iccmenber.demo02;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue(){
        //direct
        //交换机
        String exchangeName = "icc.direct";
        //队列名称
        //String queueName = "simple.queue";
        //消息
        String message1 = "hello red";
        String message2 = "hello yellow";
        String message3 = "hello blue";
        //发送消息
        //rabbitTemplate.convertAndSend(queueName, message);
        rabbitTemplate.convertAndSend(exchangeName, "red", message1);
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message2);
        rabbitTemplate.convertAndSend(exchangeName, "blue", message3);
        System.out.println("发送消息："+message1+"、"+message2+"、"+message3+"至交换机"+exchangeName);

        //topic
        //交换机
        String exchangeName2 = "icc.topic";
        //消息
        String message4 = "China weather";
        String message5 = "China news";
        String message6 = "Japen weather";
        String message7 = "Japen news";
        //发送消息
        //rabbitTemplate.convertAndSend(queueName, message);
        rabbitTemplate.convertAndSend(exchangeName2, "China.weather", message4);
        rabbitTemplate.convertAndSend(exchangeName2, "China.news", message5);
        rabbitTemplate.convertAndSend(exchangeName2, "Japen.weather", message6);
        rabbitTemplate.convertAndSend(exchangeName2, "Japen.news", message7);
        System.out.println("发送消息："+message4+"、"+message5+"、"+message6+"、"+message7+"至交换机"+exchangeName2);

        //消息转换器
        Map<String, Object> map = new HashMap<>();
        map.put("姓名","张三");
        map.put("年龄", 18);
        rabbitTemplate.convertAndSend("icc.object","",map);

    }
}
