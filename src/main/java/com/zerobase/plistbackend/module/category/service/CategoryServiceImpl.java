package com.zerobase.plistbackend.module.category.service;

import com.zerobase.plistbackend.module.category.entity.Category;
import com.zerobase.plistbackend.module.category.dto.response.CategoryResponse;
import com.zerobase.plistbackend.module.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

  private final CategoryRepository categoryRepository;

  @Override
  public List<CategoryResponse> findCategories() {
    List<Category> categories = categoryRepository.findAll();

    return categories.stream().map(CategoryResponse::of).toList();
  }
}