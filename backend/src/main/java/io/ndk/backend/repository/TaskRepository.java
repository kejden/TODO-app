package io.ndk.backend.repository;

import io.ndk.backend.entity.Category;
import io.ndk.backend.entity.Task;
import io.ndk.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByCategory(Category category);
}
