package com.baseproject.repository;

import com.baseproject.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository  extends JpaRepository<EmailVerificationToken, UUID> {
  void deleteByUser(User user);
}
