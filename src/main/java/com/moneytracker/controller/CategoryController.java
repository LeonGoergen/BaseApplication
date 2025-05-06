package com.moneytracker.controller;

import com.moneytracker.model.Category;
import com.moneytracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @PostMapping
  public Category createCategory(@RequestBody Category category) {
    return categoryRepository.save(category);
  }
}
