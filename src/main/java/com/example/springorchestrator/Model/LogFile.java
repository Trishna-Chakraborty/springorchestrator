package com.example.springorchestrator.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Entity
public class LogFile {



    @Id
    String id;
    String callFlowInstanceId;

    String callFlowRefId;
    String timeStamp;
    String api;
    String microservice;
    String payLoad;
    Status status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallFlowRefId() {
        return callFlowRefId;
    }

    public void setCallFlowRefId(String callFlowRefId) {
        this.callFlowRefId = callFlowRefId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMicroservice() {
        return microservice;
    }

    public void setMicroservice(String microservice) {
        this.microservice = microservice;
    }



    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String getCallFlowInstanceId() {
        return callFlowInstanceId;
    }

    public void setCallFlowInstanceId(String callFlowInstanceId) {
        this.callFlowInstanceId = callFlowInstanceId;
    }

    public LogFile(String callFlowInstanceId, String callFlowRefId, String api, String microservice, String payLoad) {
        this.id = UUID.randomUUID().toString();
        this.callFlowInstanceId=callFlowInstanceId;
        this.callFlowRefId = callFlowRefId;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        this.timeStamp = formattedDate;
        this.api = api;
        this.microservice = microservice;
        this.payLoad = payLoad;

    }

    public LogFile() {
        super();
    }
}
