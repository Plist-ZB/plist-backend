package com.zerobase.plistbackend.module.category.dto.response;

import com.zerobase.plistbackend.module.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

  private Long categoryId;
  private String categoryName;

  public static CategoryResponse of(Category category) {
    return CategoryResponse.builder()
        .categoryId(category.getCategoryId())
        .categoryName(category.getCategoryName())
        .build();
  }
}