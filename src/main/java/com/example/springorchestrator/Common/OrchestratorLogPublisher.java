package com.example.springorchestrator.Common;

import com.example.springorchestrator.Model.LogFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class OrchestratorLogPublisher {


    @Autowired
    Publisher publisher;

    public  void log(LogFile logFile) throws IOException {
        publisher.publishLog("log", logFile,"ORCHESTRATOR");
    }
}
