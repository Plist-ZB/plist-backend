package com.zerobase.plistbackend.module.category.dto.response;

import com.zerobase.plistbackend.module.category.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

  private Long categoryId;
  private String categoryName;

  public static CategoryResponse fromEntity(Category category) {
    return CategoryResponse.builder()
        .categoryId(category.getCategoryId())
        .categoryName(category.getCategoryName())
        .build();
  }

  public static List<CategoryResponse> fromEntityList(List<Category> categories) {
    List<CategoryResponse> categoryResponseList = new ArrayList<>();

    for (Category category : categories) {
      CategoryResponse categoryResponse = CategoryResponse.fromEntity(category);
      categoryResponseList.add(categoryResponse);
    }
    return categoryResponseList;
  }
}