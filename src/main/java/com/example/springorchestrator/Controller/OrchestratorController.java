package com.example.springorchestrator.Controller;


import com.example.springorchestrator.Model.*;
import com.example.springorchestrator.Repository.LogFileRepository;
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

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class OrchestratorController {

    @Autowired
    SagaCommandRepository sagaCommandRepository;

    @Autowired
    ServiceHostMappingRepository serviceHostMappingRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Autowired
    LogFileRepository logFileRepository;


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
    /*@PostMapping("orchestrator")
    public void postOrder(@RequestBody String json) throws IOException {

        rabbitTemplate.setReplyTimeout(60000);

        ObjectMapper objectMapper= new ObjectMapper();
        String object=null;


        LogFile logFile= new LogFile();
        logFile.setId(UUID.randomUUID().toString());
        logFile.setApi("postOrder");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        // System.out.println(formattedDate);
        logFile.setTimeStamp(formattedDate);
        logFile.setCallFlowRefId("1");
        logFile.setPayLoad(json);
        logFile.setMicroservice("customer");

        System.out.println("Requesting to customer " + " : " + json);
        object= (String) rabbitTemplate.convertSendAndReceive("customer_exchange","",json);
        if(object== null){
            System.out.println("Error in customer");
            logFile.setStatus(Status.UNFINISHED);
            logFileRepository.save(logFile);
            return;
        }

        logFile.setStatus(Status.FINISHED);
        logFileRepository.save(logFile);





       logFile= new LogFile();
        logFile.setId(UUID.randomUUID().toString());
        logFile.setApi("postOrder");
         date = new Date();
        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        // System.out.println(formattedDate);
        logFile.setTimeStamp(formattedDate);
        logFile.setCallFlowRefId("1");
        logFile.setPayLoad(json);
        logFile.setMicroservice("bank");



        Customer customer=objectMapper.readValue(object,Customer.class);
        Bank bank= new Bank();
        bank.setBalance(customer.getBalance());




        System.out.println("Requesting to bank " + " : " +bank);
        object= (String) rabbitTemplate.convertSendAndReceive("bank_exchange","",objectMapper.writeValueAsString(bank));
        if(object== null){
            logFile.setStatus(Status.UNFINISHED);
            logFileRepository.save(logFile);
            System.out.println("Error in bank");
            return;
        }
        logFile.setStatus(Status.FINISHED);
        logFileRepository.save(logFile);



        logFile= new LogFile();
        logFile.setId(UUID.randomUUID().toString());
        logFile.setApi("postOrder");
        date = new Date();
        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        // System.out.println(formattedDate);
        logFile.setTimeStamp(formattedDate);
        logFile.setCallFlowRefId("1");
        logFile.setPayLoad(json);
        logFile.setMicroservice("order");



        OrderEntity order= new OrderEntity();
        order.setOrderState(OrderState.APPROVED);

        System.out.println("Requesting to order " + " : " + order);
        object= (String) rabbitTemplate.convertSendAndReceive("order_exchange","",objectMapper.writeValueAsString(order));
        if(object== null){
            System.out.println("Error in order");
            logFile.setStatus(Status.UNFINISHED);
            logFileRepository.save(logFile);
            return;
        }
        logFile.setStatus(Status.FINISHED);
        logFileRepository.save(logFile);








    }*/



    public String jsonTocustomer(String jStr) throws IOException {

        return  jStr;

    }

    public String customerTobank(String cStr) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Customer customer=objectMapper.readValue(cStr,Customer.class);
        Bank bank= new Bank();
        bank.setId(customer.getId());
        bank.setBalance(customer.getBalance());
        String bStr= objectMapper.writeValueAsString(bank);
        return  bStr;

    }
    public String bankToorder(String bStr) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Bank bank=objectMapper.readValue(bStr,Bank.class);
        OrderEntity order= new OrderEntity();
        order.setId(bank.getId());
        order.setOrderState(OrderState.APPROVED);
        String oStr= objectMapper.writeValueAsString(order);
        return  oStr;

    }

    public String customerToorder(String cStr) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Customer customer=objectMapper.readValue(cStr,Customer.class);
        OrderEntity order= new OrderEntity();
        order.setId(customer.getId());
        order.setOrderState(OrderState.APPROVED);
        String oStr= objectMapper.writeValueAsString(order);
        return  oStr;

    }










    @PostMapping("orchestrator/{command}")
    public void postSagaCommand(@PathVariable("command") String command,@RequestBody Customer customer) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {

        rabbitTemplate.setReplyTimeout(60000);
        ObjectMapper objectMapper= new ObjectMapper();


        SagaCommand sagaCommand = sagaCommandRepository.findSagaCommandByCommand(command);
         String callFlowRefId= sagaCommand.getId();
        List<SagaStep> sagaStepList = sagaCommand.getSagaStepList();

       // String request=json;

        for(int i=1; i<=500; i++) {
            Random rand = new Random();
            int value = rand.nextInt(400);
            TimeUnit.MILLISECONDS.sleep(value);
            customer.setId(String.valueOf(i));
            String request=objectMapper.writeValueAsString(customer);
            for (SagaStep sagaStep : sagaStepList) {
                Method method = this.getClass().getDeclaredMethod(sagaStep.getBuildJsonFrom() + "To" + sagaStep.getBuildJsonTo(), String.class);
                request = (String) method.invoke(this, request);

                LogFile logFile = new LogFile(callFlowRefId, sagaStep.getEndPointName(), sagaStep.getServiceName(), request);
                System.out.println("Requesting to " + sagaStep.getServiceName()+" with endPoint " +sagaStep.getEndPointName()+ " : " + request);
                request = (String) rabbitTemplate.convertSendAndReceive(sagaStep.getServiceName() + "_exchange", sagaStep.getEndPointName(), request);
                if (request == null) {
                    System.out.println("Error in " + sagaStep.getServiceName());
                    logFile.setStatus(Status.UNFINISHED);
                    logFileRepository.save(logFile);
                    break;
                }

                logFile.setStatus(Status.FINISHED);
                logFileRepository.save(logFile);


            }
        }







    }
}
