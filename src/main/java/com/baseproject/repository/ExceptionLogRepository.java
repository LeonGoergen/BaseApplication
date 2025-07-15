package com.baseproject.repository;

import com.baseproject.model.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, UUID> {

}
