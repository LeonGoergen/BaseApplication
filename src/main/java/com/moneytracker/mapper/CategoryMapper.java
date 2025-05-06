package com.moneytracker.mapper;

import com.moneytracker.dto.CategoryDto;
import com.moneytracker.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "id", source = "category.id")
  CategoryDto toDto(Category category);
}
