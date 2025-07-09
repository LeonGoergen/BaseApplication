package com.baseproject.repository;

import com.baseproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
  Optional<User> findByEmail(String email);

  List<User> findByLastActiveDateTimeBeforeAndIsActive(LocalDateTime lastActiveDateTime, boolean isActive);
}

