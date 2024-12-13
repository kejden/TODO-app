package io.ndk.backend.service;

import io.ndk.backend.dto.CategoryDto;
import io.ndk.backend.dto.request.CategoryRequest;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategoriesOfUser(String email);
    CategoryDto getCategoryById(Long id);
    CategoryDto addCategory(CategoryRequest categoryRequest, String userEmail);
    CategoryDto updateCategory(Long id, CategoryRequest categoryRequest);
    void deleteCategory(Long id);
}
