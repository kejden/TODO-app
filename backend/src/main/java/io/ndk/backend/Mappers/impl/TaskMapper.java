package io.ndk.backend.Mappers.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.entity.TaskEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskMapper implements Mapper<TaskEntity, TaskDto> {

    private ModelMapper modelMapper;


    @Override
    public TaskDto mapTo(TaskEntity taskEntity) {
        return modelMapper.map(taskEntity, TaskDto.class);
    }

    @Override
    public TaskEntity mapFrom(TaskDto taskDto) {
        return modelMapper.map(taskDto, TaskEntity.class);
    }
}
