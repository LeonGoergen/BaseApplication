package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.User;
import com.baseproject.model.enums.UserRoleEnum;
import com.baseproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserDto save(UserCreateDto userDto) {
    User user = userMapper.toEntity(userDto);
    user.setRoles(Set.of(UserRoleEnum.USER));
    user.setPassword(passwordEncoder.encode(userDto.password()));
    user.setIsActive(true);

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new ValidationException(ExceptionEnum.USERNAME_ALREADY_EXISTS)
          .setHttpStatus(HttpStatus.CONFLICT);
    }

    if (userRepository.findByEmail(user.getUsername()).isPresent()) {
      throw new ValidationException(ExceptionEnum.EMAIL_ALREADY_EXISTS)
          .setHttpStatus(HttpStatus.CONFLICT);
    }

    return userMapper.toDto(userRepository.save(user));
  }
}
