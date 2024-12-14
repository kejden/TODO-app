package io.ndk.backend.service;

import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.dto.request.TaskRequest;

import java.util.List;

public interface TaskService {
    List<TaskDto> getTaskByUser(String email);
    List<TaskDto> getTasksWithoutCategory(String email);
    List<TaskDto> getTaskByCategoryId(Long categoryId);
    TaskDto createTask(TaskRequest request);
    TaskDto getTaskById(Long id);
    TaskDto updateById(Long id, TaskRequest task);
    void deleteById(Long id);
}
