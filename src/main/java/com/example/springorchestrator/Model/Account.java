package com.example.springorchestrator.Model;

import javax.persistence.Id;

public class Account {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
