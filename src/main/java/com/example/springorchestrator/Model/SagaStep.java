package com.example.springorchestrator.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SagaStep {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    String id;
    String name;
    String endPointName;
    String serviceName;
    String buildJsonFrom;
    String buildJsonTo;

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBuildJsonFrom() {
        return buildJsonFrom;
    }

    public void setBuildJsonFrom(String buildJsonFrom) {
        this.buildJsonFrom = buildJsonFrom;
    }

    public String getBuildJsonTo() {
        return buildJsonTo;
    }

    public void setBuildJsonTo(String buildJsonTo) {
        this.buildJsonTo = buildJsonTo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
