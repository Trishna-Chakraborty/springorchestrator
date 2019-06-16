package com.example.springorchestrator;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringorchestratorApplication {



    @Bean
    Queue replyQueue() {
        return new Queue("reply_queue", false);
    }


    @Bean
    Queue deadQueue() {
        return new Queue("dead_queue", false);
    }

    @Bean
    DirectExchange deadExchange() {
        return new DirectExchange("dead_exchange");
    }

    @Bean
    Binding deadBinding(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("");
    }







    public static void main(String[] args) {
        SpringApplication.run(SpringorchestratorApplication.class, args);
    }

}
