package com.example.springorchestrator.Test;

import com.example.springorchestrator.Model.Customer;
import com.example.springorchestrator.Model.SagaCommand;
import com.example.springorchestrator.Repository.SagaCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class TestThread implements Runnable {





     Thread t;
     int index;
     Customer customer;
     public TestThread(int index, Customer customer){
         t= new Thread(this);
         this.index=index;
         this.customer=customer;
         t.start();
     }


    @Override
    public void run() {

        Random rand = new Random();

        for(int i=index; i<=index+100; i++) {
            customer.setId(""+i);

            System.out.println("Request no "+i + " is going to be executed");
            int value = rand.nextInt(50);
            try {
                TimeUnit.MILLISECONDS.sleep(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int id = rand.nextInt(3);
            String[] arr={"postElectronicOrder","updateElectronicOrder","postOrder"};

           ;

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity("http://localhost:8080/orchestrator/" + arr[id], customer,String.class);


            System.out.println("Request no "+i + " is executed successfully");
        }


    }
}
