package com.example.springorchestrator.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    private String id;

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    private boolean isFailed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
