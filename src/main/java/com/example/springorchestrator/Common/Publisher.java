package com.example.springorchestrator.Common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Component
public class Publisher {
    @Autowired
    RabbitTemplate rabbitTemplate;



    @Autowired
    Gson gson;



    public String publish(String exchangeName,String bindingKey, String message){
        rabbitTemplate.setReplyTimeout(60000);
        System.out.println("Requesting to " + exchangeName+" with endPoint " + bindingKey+ " : " + message);
        return  (String) rabbitTemplate.convertSendAndReceive(exchangeName,bindingKey, message);
    }



    public void publishLog(String queue,Object message,String logType) throws IOException {
        ObjectWriter ow =  new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(message);
        rabbitTemplate.convertAndSend(queue, json, m -> {
            m.getMessageProperties().getHeaders().put("logType", logType);
            return m;
        });
    }

}