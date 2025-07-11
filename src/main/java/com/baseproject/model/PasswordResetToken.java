package com.baseproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "email_verification_token")
@NoArgsConstructor
public class PasswordResetToken extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private LocalDateTime expiryDate;

  public PasswordResetToken(User user) {
    this.user = user;
    this.expiryDate = LocalDateTime.now().plusHours(1);
  }

  public boolean isExpired() {
    return expiryDate.isBefore(LocalDateTime.now());
  }
}
