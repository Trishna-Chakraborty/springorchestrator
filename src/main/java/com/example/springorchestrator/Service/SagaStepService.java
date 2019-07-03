package com.example.springorchestrator.Service;


import com.example.springorchestrator.Helper.RequestBodyCustom;
import com.example.springorchestrator.Helper.RequestEntityCustom;
import com.example.springorchestrator.Helper.RequestEntityListCustom;
import com.example.springorchestrator.Model.SagaStep;
import com.example.springorchestrator.Repository.SagaStepRepository;
import org.hibernate.validator.constraints.Mod11Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SagaStepService {



    @Autowired
    SagaStepRepository sagaStepRepository;


    public RequestEntityCustom<SagaStep, String> postSagaStep(RequestEntityCustom<SagaStep,String> requestEntity){
        SagaStep sagaStep= requestEntity.data.attributes;
        sagaStep=sagaStepRepository.save(sagaStep);
        requestEntity.data.attributes=sagaStep;
        return  requestEntity;

    }

    public RequestEntityListCustom<SagaStep, String> getAll() {
        RequestEntityListCustom<SagaStep,String> requestEntityListCustom= new RequestEntityListCustom<>();
        List<SagaStep> sagaStepList=sagaStepRepository.findAll();
        for (SagaStep sagaStep:sagaStepList) {
            RequestBodyCustom<SagaStep,String> requestBodyCustom= new RequestBodyCustom<>();
            requestBodyCustom.attributes=sagaStep;
            requestBodyCustom.id=sagaStep.getId();
            requestBodyCustom.type="sagasteps";
            requestEntityListCustom.data.add(requestBodyCustom);
        }

        return  requestEntityListCustom;
    }
}
