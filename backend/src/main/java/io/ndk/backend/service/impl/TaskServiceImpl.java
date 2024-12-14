package io.ndk.backend.service.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.dto.request.TaskRequest;
import io.ndk.backend.entity.Category;
import io.ndk.backend.entity.Task;
import io.ndk.backend.entity.User;
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
    private final Mapper<Task, TaskDto> mapper;

    @Override
    public List<TaskDto> getTaskByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
        return taskRepository.findByUser(user).stream().map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksWithoutCategory(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
        return taskRepository.findByUser(user).stream().filter(t -> t.getCategory() == null).map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTaskByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        return taskRepository.findByCategory(category).stream().map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
        Category categoryEntity = null;
        if(request.getCategoryId() != null) {
            categoryEntity = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        }
        Task task = new Task().builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(categoryEntity)
                .status(request.getStatus())
                .user(user)
                .build();
        Task savedTask = taskRepository.save(task);
        return mapper.mapTo(savedTask);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_TASK));
        return mapper.mapTo(task);
    }

    @Override
    public TaskDto updateById(Long id, TaskRequest request) {
        Category categoryEntity = null;
        if(request.getCategoryId() != null) {
            categoryEntity = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
        }
        Task task = taskRepository.findById(id).orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_TASK));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setCategory(categoryEntity);
        Task saved = taskRepository.save(task);
        return mapper.mapTo(saved);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
