package io.ndk.backend.service.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.dto.request.TaskRequest;
import io.ndk.backend.entity.CategoryEntity;
import io.ndk.backend.entity.TaskEntity;
import io.ndk.backend.entity.UserEntity;
import io.ndk.backend.handler.BusinessErrorCodes;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.TaskRepository;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper<TaskEntity, TaskDto> mapper;

    @Override
    public List<TaskDto> getTaskByUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
        return taskRepository.findByUser(user).stream().map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTaskByCategoryId(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        return taskRepository.findByCategory(category).stream().map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskRequest request) {
        CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        TaskEntity task = new TaskEntity().builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(categoryEntity)
                .build();
        TaskEntity savedTask = taskRepository.save(task);
        return mapper.mapTo(savedTask);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_TASK));
        return mapper.mapTo(task);
    }

    @Override
    public TaskDto updateById(Long id, TaskRequest request) {
        CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_TASK));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategory(categoryEntity);
        TaskEntity saved = taskRepository.save(task);
        return mapper.mapTo(saved);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
