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

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAllCategoriesOfUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));

        return categoryRepository.findByUser(user).stream().map(categoryMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::mapTo)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
    }

    @Override
    public CategoryDto addCategory(CategoryRequest categoryRequest, String userEmail) {
        User userEntity = userRepository.findByEmail(userEmail).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
        if (categoryRepository.existsByNameAndUser(categoryRequest.getName(), userEntity)) {
            throw new CustomException(BusinessErrorCodes.CATEGORY_ALREADY_EXISTS);
        }
        Category categoryEntity = new Category().builder()
                .name(categoryRequest.getName())
                .user(userEntity)
                .build();
        Category savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.mapTo(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryRequest categoryRequest) {
        Category categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        categoryEntity.setName(categoryRequest.getName());
        Category savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.mapTo(savedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY);
        }
        categoryRepository.deleteById(id);
    }
}
