package com.example.springorchestrator.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class SagaCommand {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    String command;

    @ManyToMany(cascade = CascadeType.ALL)
    List<SagaStep> sagaStepList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<SagaStep> getSagaStepList() {
        return sagaStepList;
    }

    public void setSagaStepList(List<SagaStep> sagaStepList) {
        this.sagaStepList = sagaStepList;
    }
}
