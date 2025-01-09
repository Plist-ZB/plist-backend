package com.zerobase.plistbackend.module.category.service;

import com.zerobase.plistbackend.module.category.dto.response.CategoryResponse;
import java.util.List;

public interface CategoryService {

  List<CategoryResponse> findCategories();
}