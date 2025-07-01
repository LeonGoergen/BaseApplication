package com.baseproject.controller;

import com.baseproject.dto.CategoryDto;
import com.baseproject.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public CategoryDto createCategory(@Validated @RequestBody CategoryDto category) {
    return categoryService.addCategory(category.name());
  }
}
