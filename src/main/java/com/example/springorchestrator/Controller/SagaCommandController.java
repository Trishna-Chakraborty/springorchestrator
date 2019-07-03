package com.example.springorchestrator.Controller;


import com.example.springorchestrator.Helper.RequestEntityCustom;
import com.example.springorchestrator.Helper.RequestEntityListCustom;
import com.example.springorchestrator.Model.SagaCommand;
import com.example.springorchestrator.Service.SagaCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
public class SagaCommandController {


    @Autowired
    SagaCommandService sagaCommandService;


    @PostMapping("/sagacommands")
    ResponseEntity<RequestEntityCustom> postSagaCommand(@RequestBody RequestEntityCustom<SagaCommand,String> requestEntity){
        System.out.println(requestEntity);
        requestEntity=sagaCommandService.postSagaCommand(requestEntity);
        return new ResponseEntity<>(requestEntity, HttpStatus.OK);
    }

    @GetMapping("/sagacommands")
    ResponseEntity<RequestEntityListCustom> getAll(){
        RequestEntityListCustom<SagaCommand,String> requestEntityListCustom=sagaCommandService.getAll();
        return new ResponseEntity<>(requestEntityListCustom,HttpStatus.OK);

    }


}
