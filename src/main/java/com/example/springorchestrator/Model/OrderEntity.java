package com.example.springorchestrator.Model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


public class OrderEntity {

   // @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private OrderState orderState;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
