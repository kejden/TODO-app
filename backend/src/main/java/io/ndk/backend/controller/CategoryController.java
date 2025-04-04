package io.ndk.backend.controller;

import io.ndk.backend.dto.CategoryDto;
import io.ndk.backend.dto.request.CategoryRequest;
import io.ndk.backend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getCategories(
            Principal principal
    ) {
        List<CategoryDto> categories = categoryService.getAllCategoriesOfUser(principal.getName());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long id,
            Principal principal
    ) {
        CategoryDto categoryDto = categoryService.getCategoryById(id, principal);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryRequest categoryRequest,
            Principal principal
    ) {
        CategoryDto categoryDto = categoryService.addCategory(categoryRequest, principal.getName());
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest categoryRequest,
            Principal principal
    ) {
        CategoryDto categoryDto = categoryService.updateCategory(id, categoryRequest, principal);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            Principal principal
    ) {
        categoryService.deleteCategory(id, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
