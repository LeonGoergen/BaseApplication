package com.baseproject.repository;

import com.baseproject.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
  Optional<User> findByEmail(String email);

  List<User> findByLastActiveDateTimeBeforeAndIsActive(LocalDateTime lastActiveDateTime, boolean isActive);

  // bypasses Hibernate’s dirty checking mechanism, so it won’t increment the @Version field
  @Modifying
  @Query("UPDATE User u SET u.lastActiveDateTime = :lastActiveDate WHERE u.id = :id")
  void updateLastActiveDateTime(@Param("id") UUID id, @Param("lastActiveDate") LocalDateTime lastActiveDate);

}

