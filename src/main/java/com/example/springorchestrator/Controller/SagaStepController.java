package com.example.springorchestrator.Controller;


import com.example.springorchestrator.Helper.RequestEntityCustom;
import com.example.springorchestrator.Helper.RequestEntityListCustom;
import com.example.springorchestrator.Model.SagaStep;
import com.example.springorchestrator.Service.SagaStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SagaStepController {

    @Autowired
    SagaStepService sagaStepService;


    @PostMapping("/sagasteps")
    ResponseEntity<RequestEntityCustom> postSagaStep(@RequestBody RequestEntityCustom<SagaStep,String> requestEntity){
        System.out.println(requestEntity);
        requestEntity=sagaStepService.postSagaStep(requestEntity);
        return new ResponseEntity<>(requestEntity,HttpStatus.OK);
    }

    @GetMapping("/sagasteps")
    ResponseEntity<RequestEntityListCustom> getAll(){
        RequestEntityListCustom<SagaStep,String> requestEntityListCustom=sagaStepService.getAll();
        return new ResponseEntity<>(requestEntityListCustom,HttpStatus.OK);

    }
}
