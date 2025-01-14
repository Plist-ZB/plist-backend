package com.zerobase.plistbackend.module.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.category.dto.response.CategoryResponse;
import com.zerobase.plistbackend.module.category.entity.Category;
import com.zerobase.plistbackend.module.category.repository.CategoryRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

@Mock
private CategoryRepository categoryRepository;

@InjectMocks
private CategoryServiceImpl categoryService;

private List<Category> categoryList;

@BeforeEach
public void setUp() {
  Category category1 = Category.builder()
      .categoryId(1L)
      .categoryName("발라드")
      .build();

  Category category2 = Category.builder()
      .categoryId(2L)
      .categoryName("힙합")
      .build();

  categoryList = Arrays.asList(category1, category2);
}

  @Test
  void testFindCategories() {

  when(categoryRepository.findAll()).thenReturn(categoryList);

  List<CategoryResponse> categoryResponseList = categoryService.findCategories();

  assertEquals(2, categoryResponseList.size());
  assertEquals("발라드", categoryResponseList.get(0).getCategoryName());
  assertEquals("힙합", categoryResponseList.get(1).getCategoryName());
  }
}