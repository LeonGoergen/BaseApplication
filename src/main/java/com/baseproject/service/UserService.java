package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
import com.baseproject.exception.exceptions.ValidationException;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.*;
import com.baseproject.model.enums.UserRoleEnum;
import com.baseproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final EmailVerificationTokenRepository emailVerificationTokenRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  private final MailService mailService;

  public User findByEmail(String email) throws ValidationException
  {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ValidationException(ExceptionEnum.USER_NOT_FOUND)
            .setHttpStatus(HttpStatus.NOT_FOUND)
            .setReference("User " + email + " not found"));
  }

  @Transactional
  public User save(User user) {
    return userRepository.save(user);
  }

  @Transactional
  public User update(User user) {
    return userRepository.save(user);
  }

  @Transactional
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Transactional
  public User create(UserCreateDto userDto) {
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new ValidationException(ExceptionEnum.EMAIL_ALREADY_EXISTS)
          .setHttpStatus(HttpStatus.CONFLICT);
    }

    log.info("Creating user: {}", userDto.getEmail());

    User user = userMapper.toEntity(userDto);
    user.setRoles(Set.of(UserRoleEnum.GUEST));
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setIsActive(true);
    user.setIsVerified(false);
    user.setLastActiveDateTime(LocalDateTime.now());
    user.setProvider("default");

    User savedUser = userRepository.save(user);

    EmailVerificationToken verificationToken = new EmailVerificationToken(savedUser);
    verificationToken = emailVerificationTokenRepository.save(verificationToken);
    mailService.sendRegistrationMail(savedUser.getEmail(), savedUser.getFirstName(), verificationToken.getId());

    log.info("User saved successfully: {}", savedUser.getId());

    return savedUser;
  }

  @Transactional
  public User create(String email, String name, String provider) {
    log.info("Creating user with email via oAuth2: {}", email);

    String firstName = name != null ? name.split(" ")[0] : "";
    String lastName = name != null ? name.substring(firstName.length()).trim() : "";

    User user = new User();
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setRoles(Set.of(UserRoleEnum.GUEST));
    user.setIsActive(true);
    user.setIsVerified(true);
    user.setLastActiveDateTime(LocalDateTime.now());
    user.setProvider(provider);

    User savedUser = userRepository.save(user);

    log.info("User saved successfully via oAuth2: {}", savedUser.getId());

    return savedUser;
  }

  @Transactional
  public void updateLastActiveTime(User user) {
    userRepository.updateLastActiveDateTime(user.getId(), LocalDateTime.now());
  }

  @Transactional
  public List<User> findWithLastActiveDateBefore(LocalDateTime lastActiveThreshold, boolean isActive) {
    return userRepository.findByLastActiveDateTimeBeforeAndIsActive(lastActiveThreshold, isActive);
  }
}
