package com.example.springorchestrator.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SagaStep {
    @Id
    String id;
    String endPointName;
    String serviceName;
    String buildJsonFrom;
    String buildJsonTo;

    int sequence;

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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
}
