package com.moneytracker.service;

import com.moneytracker.dto.CategoryDto;
import com.moneytracker.mapper.CategoryMapper;
import com.moneytracker.model.Category;
import com.moneytracker.repository.CategoryRepository;
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
