package com.example.springorchestrator.Service;


import com.example.springorchestrator.Common.OrchestratorLogPublisher;
import com.example.springorchestrator.Common.Publisher;
import com.example.springorchestrator.Model.*;
import com.example.springorchestrator.Repository.LogFileRepository;
import com.example.springorchestrator.Repository.SagaCommandRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

@Service
public class OrchestratorService {

    @Autowired
    LogFileRepository logFileRepository;


    @Autowired
    OrchestratorLogPublisher orchestratorLogPublisher;
    @Autowired
    SagaCommandRepository sagaCommandRepository;


    @Autowired
    Publisher publisher;


     String jsonTocustomer(String jStr) throws IOException {

        return  jStr;

    }

     String customerTobank(String cStr) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Customer customer=objectMapper.readValue(cStr,Customer.class);
        Bank bank= new Bank();
        bank.setId(customer.getId());
        bank.setBalance(customer.getBalance());
        String bStr= objectMapper.writeValueAsString(bank);
        return  bStr;

    }
   String bankToorder(String bStr) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Bank bank=objectMapper.readValue(bStr,Bank.class);
        OrderEntity order= new OrderEntity();
        order.setId(bank.getId());
        order.setOrderState(OrderState.APPROVED);
        String oStr= objectMapper.writeValueAsString(order);
        return  oStr;

    }

     String customerToorder(String cStr) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Customer customer=objectMapper.readValue(cStr,Customer.class);
        OrderEntity order= new OrderEntity();
        order.setId(customer.getId());
        order.setOrderState(OrderState.APPROVED);
        String oStr= objectMapper.writeValueAsString(order);
        return  oStr;

    }

    public String postSagaCommand(String command, Customer customer) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {

        SagaCommand sagaCommand = sagaCommandRepository.findSagaCommandByCommand(command);
        String callFlowRefId= sagaCommand.getId();
        List<SagaStep> sagaStepList = sagaCommand.getSagaStepList();



        ObjectMapper objectMapper= new ObjectMapper();
        String request=objectMapper.writeValueAsString(customer);

        String callFlowInstanceId= UUID.randomUUID().toString();

        for (SagaStep sagaStep : sagaStepList) {
            Method method = this.getClass().getDeclaredMethod(sagaStep.getBuildJsonFrom() + "To" + sagaStep.getBuildJsonTo(), String.class);
            request = (String) method.invoke(this, request);

           // LogFile logFile = new LogFile(callFlowInstanceId,callFlowRefId, sagaStep.getEndPointName(), sagaStep.getServiceName(), request);

            request = (String) publisher.publish(callFlowInstanceId,sagaStep.getServiceName() + "_exchange", sagaStep.getEndPointName(), request);
            if (request == null) {
                System.out.println("Error in " + sagaStep.getServiceName());
               /* logFile.setStatus(Status.UNFINISHED);
                logFileRepository.save(logFile);
                orchestratorLogPublisher.log(logFile);*/
                break;
            }

          /*  logFile.setStatus(Status.FINISHED);
            logFileRepository.save(logFile);
            orchestratorLogPublisher.log(logFile);*/


        }



        return "done";


    }

    public String deposit(Customer customer) throws IOException {

           String callFlowInstanceId= UUID.randomUUID().toString();
           ObjectMapper objectMapper= new ObjectMapper();

           Account account= new Account();
           account.setId(customer.getId());



           String response=(String) publisher.publish(callFlowInstanceId, "account_exchange", "check.account", objectMapper.writeValueAsString(account));
           if(response== null){
               return "Error in check Account";
           }

           else if(response.equals("true")){
               response=(String) publisher.publish(callFlowInstanceId, "bank_exchange", "post.ledger", objectMapper.writeValueAsString(customer));

               if(response== null){
                   return "Error in post Ledger";
               }

               response=(String) publisher.publish( callFlowInstanceId,"bank_exchange", "post.bank", objectMapper.writeValueAsString(customer));

               if(response== null){

                   return "Error in post Bank ";
               }

           }
           else{

               Transaction transaction= new Transaction();
               transaction.setId(customer.getId());
               transaction.setFailed(true);
               response=(String) publisher.publish( callFlowInstanceId,"transaction_exchange", "post.transaction",objectMapper.writeValueAsString(transaction));

               if(response==null){

                   return "Error in transaction";
               }


           }


           return "done";
    }
}
