package com.moneytracker.model;

import com.moneytracker.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "categories")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Category extends BaseEntity {

  private String name;
}
