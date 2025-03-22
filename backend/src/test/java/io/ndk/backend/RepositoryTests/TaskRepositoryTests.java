package io.ndk.backend.RepositoryTests;

import io.ndk.backend.entity.Category;
import io.ndk.backend.entity.Task;
import io.ndk.backend.entity.TaskStatus;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.entity.User;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Category category;
    private Task saved;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        taskRepository.deleteAll();

        User user = User.builder()
                .email("task_test@example.com")
                .password("secret")
                .build();
        this.user = userRepository.save(user);

        Category category = Category.builder()
                .name("Test Category")
                .user(user)
                .build();
        this.category = categoryRepository.save(category);

        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.NEW)
                .category(category)
                .user(user)
                .build();

        this.saved = taskRepository.save(task);
    }

    @Test
    void testFindById() {
        Task found = taskRepository.findById(this.saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(this.saved.getId(), found.getId());
        assertEquals(this.saved.getTitle(), found.getTitle());
        assertEquals(this.saved.getDescription(), found.getDescription());
        assertEquals(this.saved.getStatus(), found.getStatus());
        assertEquals(this.saved.getCategory(), found.getCategory());
        assertEquals(this.saved.getUser(), found.getUser());
    }

    @Test
    void testFindByUser() {
        List<Task> found = taskRepository.findByUser(this.user);
        assertNotNull(found);
        Task task = found.get(0);
        assertNotNull(task);
        assertEquals(this.saved.getId(), task.getId());
        assertEquals(this.saved.getTitle(), task.getTitle());
        assertEquals(this.saved.getDescription(), task.getDescription());
        assertEquals(this.saved.getStatus(), task.getStatus());
        assertEquals(this.saved.getCategory(), task.getCategory());
        assertEquals(this.saved.getUser(), task.getUser());
    }

    @Test
    void testFindByCategory(){
        List<Task> found = taskRepository.findByCategory(this.category);
        assertNotNull(found);
        Task task = found.get(0);
        assertNotNull(task);
        assertEquals(this.saved.getId(), task.getId());
        assertEquals(this.saved.getTitle(), task.getTitle());
        assertEquals(this.saved.getDescription(), task.getDescription());
        assertEquals(this.saved.getStatus(), task.getStatus());
        assertEquals(this.saved.getCategory(), task.getCategory());
        assertEquals(this.saved.getUser(), task.getUser());
    }
}
