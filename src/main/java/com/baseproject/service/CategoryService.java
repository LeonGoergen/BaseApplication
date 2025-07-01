package com.baseproject.service;

import com.baseproject.dto.CategoryDto;
import com.baseproject.mapper.CategoryMapper;
import com.baseproject.model.Category;
import com.baseproject.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CategoryDto addCategory(String name) {
    Category category = new Category();
    category.setName(name);
    Category savedCategory = categoryRepository.save(category);
    return categoryMapper.toDto(savedCategory);
  }
}
