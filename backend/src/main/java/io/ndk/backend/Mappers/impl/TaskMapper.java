package io.ndk.backend.Mappers.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.entity.Task;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskMapper implements Mapper<Task, TaskDto> {

    private ModelMapper modelMapper;


    @Override
    public TaskDto mapTo(Task taskEntity) {
        return modelMapper.map(taskEntity, TaskDto.class);
    }

    @Override
    public Task mapFrom(TaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }
}
