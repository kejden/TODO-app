package io.ndk.backend.service.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.Mappers.impl.TaskMapper;
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
    private final TaskMapper mapper;

    @Override
    public List<TaskDto> getTaskByUser(String email) {
        User user = getCurrentUser(email);
        return taskRepository.findByUser(user).stream().map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksWithoutCategory(String email) {
        User user = getCurrentUser(email);
        return taskRepository.findByUser(user).stream().filter(t -> t.getCategory() == null).map(mapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTaskByCategoryId(Long categoryId, String email) {
        User user = getCurrentUser(email);
        Category category = getCategoryById(categoryId);

        if (!category.getUser().getEmail().equals(user.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        return taskRepository.findByCategory(category)
                .stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskRequest request, String email) {
        User user = getCurrentUser(email);
        Category categoryEntity = null;
        if(request.getCategoryId() != null) {
            categoryEntity = getCategoryById(request.getCategoryId());

            if (!categoryEntity.getUser().getEmail().equals(user.getEmail())) {
                throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
            }
        }
        Task task = Task.builder()
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
    public TaskDto getTaskById(Long id, String email) {
        User user = getCurrentUser(email);
        Task task = getTaskById(id);

        if (!task.getUser().getEmail().equals(user.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        return mapper.mapTo(task);
    }

    @Override
    public TaskDto updateById(Long id, TaskRequest request, String email) {
        User user = getCurrentUser(email);
        Task task = getTaskById(id);

        if (!task.getUser().getEmail().equals(user.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        Category categoryEntity = null;
        if (request.getCategoryId() != null) {
            categoryEntity = getCategoryById(request.getCategoryId());
            if (!categoryEntity.getUser().getEmail().equals(user.getEmail())) {
                throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
            }
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setCategory(categoryEntity);
        Task saved = taskRepository.save(task);
        return mapper.mapTo(saved);
    }

    @Override
    public void deleteById(Long id, String email) {
        User user = getCurrentUser(email);
        Task task = getTaskById(id);

        if (!task.getUser().getEmail().equals(user.getEmail())) {
            throw new CustomException(BusinessErrorCodes.ACCESS_DENIED);
        }

        taskRepository.deleteById(id);
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_EMAIL));
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_CATEGORY));
    }

    private Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NO_SUCH_TASK));
    }
}
