package com.zerobase.plistbackend.module.category.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.category.dto.response.CategoryResponse;
import com.zerobase.plistbackend.module.category.service.CategoryServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

  @Mock
  private CategoryServiceImpl categoryService;

  @InjectMocks
  private CategoryController categoryController;

  @Test
  void testFindCategories() {
    //given
    CategoryResponse category1 = CategoryResponse.builder()
        .categoryId(1L)
        .categoryName("발라드")
        .build();

    CategoryResponse category2 = CategoryResponse.builder()
        .categoryId(2L)
        .categoryName("힙합")
        .build();

    List<CategoryResponse> categoryResponseList = new ArrayList<>(
        Arrays.asList(category1, category2));
    when(categoryService.findCategories()).thenReturn(categoryResponseList);
    //when
    ResponseEntity<List<CategoryResponse>> responses = categoryController.findCategories();
    //then
    assertEquals(categoryResponseList, responses.getBody());
  }
}