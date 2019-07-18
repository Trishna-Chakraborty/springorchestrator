package com.example.springorchestrator.Common;

import com.example.springorchestrator.Model.LogFile;
import com.example.springorchestrator.Model.Status;
import com.example.springorchestrator.Repository.LogFileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Component
public class Publisher {



    /*@Value("${replyTimeOut}")
    private  long replyTimeOut;*/

    @Autowired
    RabbitTemplate rabbitTemplate;



    @Autowired
    Gson gson;


    @Autowired
    OrchestratorLogPublisher orchestratorLogPublisher;


    @Autowired
    LogFileRepository logFileRepository;



    public String publish(String callFlowInstanceId,String exchangeName,String bindingKey, String message) throws IOException {

        //rabbitTemplate.setReplyTimeout(replyTimeOut);
        System.out.println("Requesting to " + exchangeName+" with endPoint " + bindingKey+ " : " + message);
        String result=  (String) rabbitTemplate.convertSendAndReceive(exchangeName,bindingKey, message);
        LogFile logFile = new LogFile(callFlowInstanceId,"1", bindingKey,exchangeName, message);
        if(result==null) {
            logFile.setStatus(Status.UNFINISHED);
        }
        else{
            logFile.setStatus(Status.FINISHED);
        }
        logFileRepository.save(logFile);
        orchestratorLogPublisher.log(logFile);
        return result;

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