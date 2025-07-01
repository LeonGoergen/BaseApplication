package com.baseproject.mapper;

import com.baseproject.dto.CategoryDto;
import com.baseproject.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "id", source = "category.id")
  CategoryDto toDto(Category category);
}
