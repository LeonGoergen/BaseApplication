package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
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

  public User findByEmail(String email) throws ValidationException {
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
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Transactional
  public UserDto createUser(UserCreateDto userDto) {
    log.info("Creating user: {}", userDto.email());

    User user = userMapper.toEntity(userDto);
    user.setRoles(Set.of(UserRoleEnum.GUEST));
    user.setPassword(passwordEncoder.encode(userDto.password()));
    user.setIsActive(true);
    user.setIsVerified(false);
    user.setLastActiveDateTime(LocalDateTime.now());

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new ValidationException(ExceptionEnum.EMAIL_ALREADY_EXISTS)
          .setHttpStatus(HttpStatus.CONFLICT);
    }

    User savedUser = userRepository.save(user);

    EmailVerificationToken verificationToken = new EmailVerificationToken(savedUser);
    verificationToken = emailVerificationTokenRepository.save(verificationToken);
    mailService.sendRegistrationMail(savedUser.getEmail(), savedUser.getFirstName(), verificationToken.getId());

    log.info("User saved successfully: {}", savedUser.getId());

    return userMapper.toDto(savedUser);
  }

  @Transactional
  public void updateLastActiveTime(User user) {
    user.setLastActiveDateTime(LocalDateTime.now());
    userRepository.save(user);
  }

  @Transactional
  public List<User> findWithLastActiveDateBefore(LocalDateTime lastActiveThreshold, boolean isActive) {
    return userRepository.findByLastActiveDateTimeBeforeAndIsActive(lastActiveThreshold, isActive);
  }
}
