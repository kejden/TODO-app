package io.ndk.backend.service.impl;

import io.ndk.backend.Mappers.impl.CategoryMapper;
import io.ndk.backend.dto.CategoryDto;
import io.ndk.backend.dto.request.CategoryRequest;
import io.ndk.backend.entity.Category;
import io.ndk.backend.entity.User;
import io.ndk.backend.handler.BusinessErrorCodes;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.security.Principal;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAllCategoriesOfUser(String email) {
        User user = getCurrentUser(email);

        return categoryRepository.findByUser(user)
                .stream()
                .map(categoryMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id, Principal principal) {
        User currentUser = getCurrentUser(principal.getName());

        Category categoryEntity = getCategoryById(id);

        if (!categoryEntity.getUser().getEmail().equals(currentUser.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        return categoryMapper.mapTo(categoryEntity);
    }

    @Override
    public CategoryDto addCategory(CategoryRequest categoryRequest, String userEmail) {
        User user = getCurrentUser(userEmail);

        if (categoryRepository.existsByNameAndUser(categoryRequest.getName(), user)) {
            throw new CustomException(BusinessErrorCodes.CATEGORY_ALREADY_EXISTS);
        }

        Category categoryEntity = Category.builder()
                .name(categoryRequest.getName())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.mapTo(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryRequest categoryRequest, Principal principal) {
        User currentUser = getCurrentUser(principal.getName());

        Category categoryEntity = getCategoryById(id);

        if (!categoryEntity.getUser().getEmail().equals(currentUser.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        categoryEntity.setName(categoryRequest.getName());
        Category savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.mapTo(savedCategory);
    }

    @Override
    public void deleteCategory(Long id, Principal principal) {
        User currentUser = getCurrentUser(principal.getName());

        if (!categoryRepository.existsById(id)) {
            throw new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY);
        }

        Category categoryEntity = getCategoryById(id);

        if (!categoryEntity.getUser().getEmail().equals(currentUser.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        categoryRepository.deleteById(id);
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
    }

}
