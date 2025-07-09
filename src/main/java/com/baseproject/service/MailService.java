package com.baseproject.service;

import com.baseproject.exception.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

  @Value("${mail.display-name}")
  private String displayName;

  @Value("${spring.mail.username}")
  private String fromEmail;

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Async
  public void sendRegistrationMail(String to, String name, String confirmationUrl) {
    Context context = new Context();
    context.setVariable("name", name);
    context.setVariable("confirmationUrl", confirmationUrl);

    String htmlContent = templateEngine.process("registration-template.html", context);
    sendMail(to, "Registration Confirmation", htmlContent);
  }

  private void sendMail(String to, String subject, String htmlContent) {
    try {
      log.info("Sending mail to {}, subject: {}", to, subject);
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);
      helper.setFrom(fromEmail, displayName);

      mailSender.send(message);
      log.info("Mail sent successfully to {}", to);
    } catch (MessagingException | UnsupportedEncodingException e) {
      throw new ServiceFailedException(ExceptionEnum.MAIL_SERVICE_NOT_AVAILABLE)
          .setReference("Failed to send email [" + subject + "] to " + to);
    }
  }
}
