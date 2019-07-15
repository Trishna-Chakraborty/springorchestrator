package com.example.springorchestrator.Controller;


import com.example.springorchestrator.Common.OrchestratorLogPublisher;
import com.example.springorchestrator.Common.Publisher;
import com.example.springorchestrator.Model.*;
import com.example.springorchestrator.Repository.LogFileRepository;
import com.example.springorchestrator.Repository.SagaCommandRepository;
import com.example.springorchestrator.Repository.ServiceHostMappingRepository;
import com.example.springorchestrator.Service.OrchestratorService;
import com.example.springorchestrator.SpringorchestratorApplication;
import com.example.springorchestrator.Test.TestThread;
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
import java.util.*;
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


    @Autowired
    OrchestratorLogPublisher orchestratorLogPublisher;


    @Autowired
    Publisher publisher;


    @Autowired
    OrchestratorService orchestratorService;


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






/*

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
*/






   @PostMapping("orchestrator")
   public void postSagaArbitrary(@RequestBody  Customer customer) throws InterruptedException {

       Random rand = new Random();

        for(int i=1; i<=100; i++) {
            customer.setId(""+i);

            System.out.println("Request no "+i + " is going to be executed");
            int value = rand.nextInt(50);
            TimeUnit.MILLISECONDS.sleep(value);

            int id = rand.nextInt(3) +1;
            SagaCommand sagaCommand = sagaCommandRepository.findById(id+"").orElse(null);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity("http://localhost:8080/orchestrator/" + sagaCommand.getCommand(), customer,String.class);


            System.out.println("Request no "+i + " is executed successfully");
        }




   }

    @PostMapping("saga/threadCheck")
    public void threadCheck(@RequestBody  Customer customer) throws InterruptedException {

        for(int i=0; i<10; i++){

            TestThread t= new TestThread(i,customer);
        }




    }





    /*@PostMapping("orchestrator/{command}")
    public String postSagaCommand(@PathVariable("command") String command,@RequestBody Customer customer) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {



        rabbitTemplate.setReplyTimeout(60000);
        ObjectMapper objectMapper= new ObjectMapper();


        SagaCommand sagaCommand = sagaCommandRepository.findSagaCommandByCommand(command);
         String callFlowRefId= sagaCommand.getId();
        List<SagaStep> sagaStepList = sagaCommand.getSagaStepList();

       // String request=json;





        //for(int i=1; i<=50; i++) {
           // Random rand = new Random();
         //   int value = rand.nextInt(400);
          //  TimeUnit.MILLISECONDS.sleep(value);
      //      customer.setId(String.valueOf(i));
            String request=objectMapper.writeValueAsString(customer);
            String callFlowInstanceId=UUID.randomUUID().toString();
            for (SagaStep sagaStep : sagaStepList) {
                Method method = this.getClass().getDeclaredMethod(sagaStep.getBuildJsonFrom() + "To" + sagaStep.getBuildJsonTo(), String.class);
                request = (String) method.invoke(this, request);

                LogFile logFile = new LogFile(callFlowInstanceId,callFlowRefId, sagaStep.getEndPointName(), sagaStep.getServiceName(), request);

                System.out.println("Requesting to " + sagaStep.getServiceName()+" with endPoint " +sagaStep.getEndPointName()+ " : " + request);
                request = (String) rabbitTemplate.convertSendAndReceive(sagaStep.getServiceName() + "_exchange", sagaStep.getEndPointName(), request);
                if (request == null) {
                    System.out.println("Error in " + sagaStep.getServiceName());
                    logFile.setStatus(Status.UNFINISHED);
                    logFileRepository.save(logFile);
                    orchestratorLogPublisher.log(logFile);
                    break;
                }

                logFile.setStatus(Status.FINISHED);
                logFileRepository.save(logFile);
                orchestratorLogPublisher.log(logFile);


            }
       // }



    return "done";


    }*/


    @PostMapping("orchestrator/{command}")
    public String postCommand(@PathVariable("command") String command,@RequestBody Customer customer) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {

          return orchestratorService.postSagaCommand(command,customer);
    }

    @PostMapping("deposit")
    public String postCommand(@RequestBody Customer customer) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {

        return orchestratorService.deposit(customer);
    }
}
