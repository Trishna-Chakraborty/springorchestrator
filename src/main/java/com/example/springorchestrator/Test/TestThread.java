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





   /*  Thread t;
     int index;
     Customer customer;
     public TestThread(int index, Customer customer){
         t= new Thread(this);
         this.index=index;
         this.customer=customer;
         t.start();
     }*/


  /*  @Override
    public void run() {

        Random rand = new Random();

        for(int i=index; i<index+100; i++) {
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


    }*/



    public int max=100;
    static int  number=0;
    int remainder;
    static Object lock=new Object();
    Thread t;
    Customer customer;

    public TestThread(int remainder,Customer customer)
    {

        t=new Thread(this,""+remainder);
        this.remainder=remainder;
        this.customer=customer;
        t.start();
    }

    @Override
    public void run() {
        while (number < max-1) {
            synchronized (lock) {
                while (number % 10 != remainder) { // wait for numbers other than remainder
                   if(number>max) break;
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(number>max) break;
                System.out.println(Thread.currentThread().getName() + ": " + number);
                customer.setId(""+number);
                Random rand=new Random();


                int value = rand.nextInt(50);
                try {
                    TimeUnit.MILLISECONDS.sleep(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               /* int id = rand.nextInt(3);
                String[] arr={"postElectronicOrder","updateElectronicOrder","postOrder"};



                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForEntity("http://localhost:8080/orchestrator/" + arr[id], customer,String.class);


                System.out.println("Request no "+number + " is executed successfully");*/
                number++;
                lock.notifyAll();
            }
            System.out.println("Request no "+number + " is going to be executed");
            Random rand=new Random();
            int id = rand.nextInt(3);
            String[] arr={"postElectronicOrder","updateElectronicOrder","postOrder"};



            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity("http://localhost:8080/orchestrator/" + arr[id], customer,String.class);


            System.out.println("Request no "+number + " is executed successfully");



        }
    }
}
