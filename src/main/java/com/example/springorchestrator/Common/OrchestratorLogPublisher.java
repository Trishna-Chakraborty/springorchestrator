package com.example.springorchestrator.Common;

import com.example.springorchestrator.Model.LogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrchestratorLogPublisher {


    @Autowired
    Publisher publisher;

    public  void log(LogFile logFile) throws IOException {
        publisher.publishLog("log", logFile,"ORCHESTRATOR");
    }
}
