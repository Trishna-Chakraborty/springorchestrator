package com.example.springorchestrator.Model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

public class Bank {

    //@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private double balance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
