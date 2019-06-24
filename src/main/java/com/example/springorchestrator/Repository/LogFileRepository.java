package com.example.springorchestrator.Repository;

import com.example.springorchestrator.Model.LogFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogFileRepository extends JpaRepository<LogFile, Integer> {
}
