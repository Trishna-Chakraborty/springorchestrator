package com.example.springorchestrator.Controller;


import com.example.springorchestrator.Model.*;
import com.example.springorchestrator.Repository.SagaCommandRepository;
import com.example.springorchestrator.Repository.ServiceHostMappingRepository;
import com.example.springorchestrator.SpringorchestratorApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
public class OrchestratorController {

    @Autowired
    SagaCommandRepository sagaCommandRepository;

    @Autowired
    ServiceHostMappingRepository serviceHostMappingRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @GetMapping("orchestrator/{command}")
    public void getOrder(@PathVariable("command")String command,String json) {

        rabbitTemplate.setReplyTimeout(60000);
        //rabbitTemplate.setUseTemporaryReplyQueues(false);
        SagaCommand sagaCommand = sagaCommandRepository.findSagaCommandByCommand(command);
        List<SagaStep> sagaStepList = sagaCommand.getSagaStepList();


        Object response="100";
        for (SagaStep sagaStep : sagaStepList) {
            System.out.println("Requesting to "+ sagaStep.getServiceName() + " : " + response);
            response =rabbitTemplate.convertSendAndReceive(sagaStep.getServiceName() + "_exchange","",response);
            if(response== null){
                System.out.println("Error in "+ sagaStep.getServiceName());
                break;
            }

        }








    }
    @PostMapping("orchestrator")
    public void postOrder(@RequestBody String json) throws IOException {

        rabbitTemplate.setReplyTimeout(60000);
        ObjectMapper objectMapper= new ObjectMapper();

        String object=null;
        System.out.println("Requesting to customer " + " : " + json);
        object= (String) rabbitTemplate.convertSendAndReceive("customer_exchange","",json);
        if(object== null){
            System.out.println("Error in customer");
            return;
        }

        Customer customer=objectMapper.readValue(object,Customer.class);
        Bank bank= new Bank();
        bank.setBalance(customer.getBalance());


        /* message = MessageBuilder
                .withBody(strMsg.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();*/
        System.out.println("Requesting to bank " + " : " +bank);
        object= (String) rabbitTemplate.convertSendAndReceive("bank_exchange","",objectMapper.writeValueAsString(bank));
        if(object== null){
            System.out.println("Error in bank");
            return;
        }



        //bank=objectMapper.readValue(strMsg, Bank.class);
        OrderEntity order= new OrderEntity();
        order.setOrderState(OrderState.APPROVED);
        //strMsg= objectMapper.writeValueAsString(order);
/*
        message = MessageBuilder
                .withBody(strMsg.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();*/
        System.out.println("Requesting to order " + " : " + order);
        object= (String) rabbitTemplate.convertSendAndReceive("order_exchange","",objectMapper.writeValueAsString(order));
        if(object== null){
            System.out.println("Error in order");
            return;
        }








    }
}
