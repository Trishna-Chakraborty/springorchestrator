package com.example.springorchestrator.Repository;


import com.example.springorchestrator.Model.SagaCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaCommandRepository extends JpaRepository<SagaCommand, Integer> {


    public SagaCommand findSagaCommandByCommand(String name);
}
