package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.User;
import com.baseproject.model.enums.UserRoleEnum;
import com.baseproject.repository.UserRepository;
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
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  private final MailService mailService;

  public User findByUsername(String username) throws ValidationException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ValidationException(ExceptionEnum.USER_NOT_FOUND)
            .setHttpStatus(HttpStatus.NOT_FOUND)
            .setReference("User with username " + username + " not found"));
  }

  public UserDto findDtoByUsername(String username) throws ValidationException {
    User user = findByUsername(username);
    return userMapper.toDto(user);
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
    log.info("Creating user: {}", userDto.username());

    User user = userMapper.toEntity(userDto);
    user.setRoles(Set.of(UserRoleEnum.GUEST));
    user.setPassword(passwordEncoder.encode(userDto.password()));
    user.setIsActive(true);
    user.setLastActiveDateTime(LocalDateTime.now());

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new ValidationException(ExceptionEnum.USERNAME_ALREADY_EXISTS)
          .setHttpStatus(HttpStatus.CONFLICT);
    }

    if (userRepository.findByEmail(user.getUsername()).isPresent()) {
      throw new ValidationException(ExceptionEnum.EMAIL_ALREADY_EXISTS)
          .setHttpStatus(HttpStatus.CONFLICT);
    }

    User savedUser = userRepository.save(user);

    mailService.sendRegistrationMail(savedUser.getEmail(), savedUser.getFirstName(), "https://youtube.com");

    log.info("User saved successfully: {}", savedUser.getUsername());

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
