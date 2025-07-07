package com.baseproject.model;

import com.baseproject.model.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Audited
public class User extends BaseEntity  {

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "address")
  private String address;

  @Column(name = "city")
  private String city;

  @Column(name = "country")
  private String country;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Set<UserRoleEnum> roles;

  @Column(name = "last_active_date_time")
  private LocalDateTime lastActiveDateTime;
}
