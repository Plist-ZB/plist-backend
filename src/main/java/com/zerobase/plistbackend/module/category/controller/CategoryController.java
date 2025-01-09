package com.zerobase.plistbackend.module.category.controller;

import com.zerobase.plistbackend.module.category.dto.response.CategoryResponse;
import com.zerobase.plistbackend.module.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/api")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "카테고리와 관련된 API Controller")
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(
      summary = "카테고리 전체 목록 조회.",
      description = "카테고리 전체 목록을 조회합니다."
  )
  @GetMapping("/categories")
  public ResponseEntity<List<CategoryResponse>> findCategories() {

    List<CategoryResponse> categoryResponseList = categoryService.findCategories();

    return ResponseEntity.ok(categoryResponseList);
  }
}