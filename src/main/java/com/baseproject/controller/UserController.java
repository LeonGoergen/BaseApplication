package com.baseproject.controller;

import com.baseproject.dto.*;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.User;
import com.baseproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  public UserDto createUser(@RequestBody @Validated UserCreateDto user) {
    User userDto = userService.create(user);
    return userMapper.toDto(userDto);
  }
}
