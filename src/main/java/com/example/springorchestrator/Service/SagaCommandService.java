package com.example.springorchestrator.Service;


import com.example.springorchestrator.Helper.RequestBodyCustom;
import com.example.springorchestrator.Helper.RequestEntityCustom;
import com.example.springorchestrator.Helper.RequestEntityListCustom;
import com.example.springorchestrator.Model.SagaCommand;
import com.example.springorchestrator.Repository.SagaCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SagaCommandService {

    @Autowired
    SagaCommandRepository sagaCommandRepository;

    public RequestEntityCustom<SagaCommand, String> postSagaCommand(RequestEntityCustom<SagaCommand, String> requestEntity) {


        SagaCommand sagaCommand= requestEntity.data.attributes;
        sagaCommand=sagaCommandRepository.save(sagaCommand);
        requestEntity.data.attributes=sagaCommand;
        requestEntity.data.id=sagaCommand.getId();
        return  requestEntity;

    }

    public RequestEntityListCustom<SagaCommand, String> getAll() {

        RequestEntityListCustom<SagaCommand,String> requestEntityListCustom= new RequestEntityListCustom<>();
        List<SagaCommand> sagaCommandList=sagaCommandRepository.findAll();
        for (SagaCommand sagaCommand:sagaCommandList) {
            RequestBodyCustom<SagaCommand,String> requestBodyCustom= new RequestBodyCustom<>();
            requestBodyCustom.attributes=sagaCommand;
            requestBodyCustom.id=sagaCommand.getId();
            requestBodyCustom.type="sagacommands";
            requestEntityListCustom.data.add(requestBodyCustom);
        }

        return  requestEntityListCustom;

    }
}
