package com.example.springorchestrator.Model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

public class Bank {

    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private double balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
