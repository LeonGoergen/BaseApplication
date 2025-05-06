package com.moneytracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "categories")
@Data
@Accessors(chain = true)
public class Category {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
}
