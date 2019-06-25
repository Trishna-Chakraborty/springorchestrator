package com.example.springorchestrator.Model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


public class OrderEntity {

   // @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private OrderState orderState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
