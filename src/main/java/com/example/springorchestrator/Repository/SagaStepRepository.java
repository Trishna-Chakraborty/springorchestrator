package com.example.springorchestrator.Repository;

import com.example.springorchestrator.Model.SagaStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaStepRepository extends JpaRepository<SagaStep,String> {
}
