package com.baseproject.model.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN"),
  EDITOR("ROLE_EDITOR");

  private final String role;

  UserRoleEnum(String role) {
      this.role = role;
  }

}
