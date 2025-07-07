package com.baseproject.model.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
  GUEST("ROLE_GUEST"),
  PREMIUM("ROLE_PREMIUM"),
  ADMIN("ROLE_ADMIN"),
  EDITOR("ROLE_EDITOR");

  private final String role;

  UserRoleEnum(String role) {
      this.role = role;
  }

}
