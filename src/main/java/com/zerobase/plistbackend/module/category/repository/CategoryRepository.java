package com.zerobase.plistbackend.module.category.repository;

import com.zerobase.plistbackend.module.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}