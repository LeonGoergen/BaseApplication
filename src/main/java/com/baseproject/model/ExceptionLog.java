package com.baseproject.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "exception_log")
@Data
public class ExceptionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column
  private OffsetDateTime timestamp;

  @Column
  private String exceptionType;

  @Column
  private String message;

  @Column
  private String method;

  @Column
  private boolean isRead;

  @Column
  private boolean isHandled;
}
