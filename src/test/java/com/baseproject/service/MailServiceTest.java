package com.baseproject.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Disabled
public class MailServiceTest {

  @Autowired
  private MailService mailService;

  @Test
  public void testSendRegistrationMail() {
    mailService.sendRegistrationMail("leon.goergen@gmail.com", "Leon", UUID.randomUUID());
  }

  @Test
  public void testSendInactivityWarningMail()
  {
    mailService.sendInactivityWarningMail("leon.goergen@gmail.com", "Leon");
  }

  @Test
  public void testSendDeletionWarningMail()
  {
    mailService.sendDeletionWarningMail("leon.goergen@gmail.com", "Leon", LocalDate.now().plusDays(7));
  }
}
