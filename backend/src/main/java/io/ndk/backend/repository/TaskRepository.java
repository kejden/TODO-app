package io.ndk.backend.repository;

import io.ndk.backend.entity.CategoryEntity;
import io.ndk.backend.entity.TaskEntity;
import io.ndk.backend.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    List<TaskEntity> findByUser(UserEntity user);
    List<TaskEntity> findByCategory(CategoryEntity category);
}
