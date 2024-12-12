package io.ndk.backend.Mappers.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.CategoryDto;
import io.ndk.backend.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryMapper implements Mapper<CategoryEntity, CategoryDto> {
    private ModelMapper modelMapper;

    @Override
    public CategoryDto mapTo(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }

    @Override
    public CategoryEntity mapFrom(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, CategoryEntity.class);
    }
}
