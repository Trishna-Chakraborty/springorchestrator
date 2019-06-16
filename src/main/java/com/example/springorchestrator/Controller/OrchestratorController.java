package com.example.springorchestrator.Controller;


import com.example.springorchestrator.Model.SagaCommand;
import com.example.springorchestrator.Model.SagaStep;
import com.example.springorchestrator.Model.ServiceHostMapping;
import com.example.springorchestrator.Repository.SagaCommandRepository;
import com.example.springorchestrator.Repository.ServiceHostMappingRepository;
import com.example.springorchestrator.SpringorchestratorApplication;
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
    public void postOrder(@PathVariable("command")String command) {

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
}
