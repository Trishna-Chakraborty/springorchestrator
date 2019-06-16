package com.example.springorchestrator.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServiceHostMapping {
    @Id
    private int id;
    private String serviceName;
    private String host;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
