package com.baseproject.repository;

import com.baseproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}

