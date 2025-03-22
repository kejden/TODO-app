package io.ndk.backend.ServiceTests;

import io.ndk.backend.Mappers.impl.TaskMapper;
import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.dto.request.TaskRequest;
import io.ndk.backend.entity.Category;
import io.ndk.backend.entity.Task;
import io.ndk.backend.entity.TaskStatus;
import io.ndk.backend.entity.User;
import io.ndk.backend.handler.BusinessErrorCodes;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.TaskRepository;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TaskServiceTests {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testGetTaskByUser_Empty() {
        User user = User.builder()
                .email("test@mock.com")
                .build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(user));
        when(taskRepository.findByUser(user)).thenReturn(Collections.emptyList());

        assertTrue(taskService.getTaskByUser("test@mock.com").isEmpty());
    }

    @Test
    void testGetTasksWithoutCategory_Success() {
        User user = User.builder()
                .email("test@mock.com")
                .build();
        Task task = Task.builder()
                .id(1L)
                .title("NoCatTask")
                .user(user)
                .category(null)
                .build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(user));
        when(taskRepository.findByUser(user)).thenReturn(Collections.singletonList(task));

        TaskDto mappedDto = TaskDto.builder()
                .id(1L)
                .title("NoCatTask")
                .build();
        when(mapper.mapTo(task)).thenReturn(mappedDto);

        assertEquals(1, taskService.getTasksWithoutCategory("test@mock.com").size());
    }

    @Test
    void testGetTaskByCategoryId_Success() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        Category cat = Category.builder()
                .id(123L)
                .user(mockUser)
                .build();
        Task task = Task.builder()
                .id(1L)
                .title("TaskInCat")
                .user(mockUser)
                .category(cat)
                .build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(123L)).thenReturn(Optional.of(cat));
        when(taskRepository.findByCategory(cat)).thenReturn(Collections.singletonList(task));

        TaskDto mappedDto = TaskDto.builder()
                .id(1L)
                .title("TaskInCat")
                .build();
        when(mapper.mapTo(task)).thenReturn(mappedDto);

        assertEquals(1, taskService.getTaskByCategoryId(123L, "test@mock.com").size());
    }

    @Test
    void testGetTaskByCategoryId_AccessDenied() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        User otherUser = User.builder()
                .email("someone@mock.com")
                .build();
        Category cat = Category.builder()
                .id(123L)
                .user(otherUser)
                .build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(123L)).thenReturn(Optional.of(cat));

        CustomException ex = assertThrows(CustomException.class,
                () -> taskService.getTaskByCategoryId(123L, "test@mock.com"));
        assertEquals(BusinessErrorCodes.ACCESS_DENIED, ex.getErrorCode());
    }

    @Test
    void testCreateTask_SuccessNoCategory() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        TaskRequest req = TaskRequest.builder()
                .title("TestTask")
                .description("Description")
                .status(TaskStatus.NEW)
                .categoryId(null)
                .build();

        Task savedTask = Task.builder()
                .id(1L)
                .title("TestTask")
                .user(mockUser)
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDto mappedDto = TaskDto.builder()
                .id(1L)
                .title("TestTask")
                .build();
        when(mapper.mapTo(savedTask)).thenReturn(mappedDto);

        TaskDto dto = taskService.createTask(req, "test@mock.com");
        assertEquals("TestTask", dto.getTitle());
        assertEquals(1L, dto.getId());
    }

    @Test
    void testCreateTask_AccessDeniedForCategory() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        User otherUser = User.builder()
                .email("some@mock.com")
                .build();
        Category cat = Category.builder()
                .id(555L)
                .user(otherUser)
                .name("OtherCat")
                .build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(555L)).thenReturn(Optional.of(cat));

        TaskRequest req = TaskRequest.builder()
                .title("InvalidCategoryTask")
                .categoryId(555L)
                .build();

        assertThrows(CustomException.class, () -> taskService.createTask(req, "test@mock.com"));
    }

    @Test
    void testCreateTask_NoSuchCategory() {
        User mockUser = User.builder().email("test@mock.com").build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        TaskRequest request = TaskRequest.builder()
                .title("TestTask")
                .description("Attempting with unknown category")
                .status(TaskStatus.NEW)
                .categoryId(999L)
                .build();

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> taskService.createTask(request, "test@mock.com"));
        assertEquals(BusinessErrorCodes.NO_SUCH_CATEGORY, ex.getErrorCode());
    }

    @Test
    void testCreateTask_NoSuchUser() {
        when(userRepository.findByEmail("missing@mock.com")).thenReturn(Optional.empty());

        TaskRequest request = TaskRequest.builder()
                .title("Test Missing User")
                .description("Should fail")
                .status(TaskStatus.NEW)
                .build();

        CustomException ex = assertThrows(CustomException.class,
                () -> taskService.createTask(request, "missing@mock.com"));
        assertEquals(BusinessErrorCodes.NO_SUCH_EMAIL, ex.getErrorCode());
    }

    @Test
    void testGetTaskById_Success() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com"))
                .thenReturn(Optional.of(mockUser));

        Task task = Task.builder()
                .id(9L)
                .user(mockUser)
                .title("Task Title")
                .build();
        when(taskRepository.findById(9L)).thenReturn(Optional.of(task));

        TaskDto mappedDto = TaskDto.builder()
                .id(9L)
                .title("Task Title")
                .build();
        when(mapper.mapTo(task)).thenReturn(mappedDto);

        TaskDto dto = taskService.getTaskById(9L, "test@mock.com");
        assertEquals("Task Title", dto.getTitle());
        assertEquals(9L, dto.getId());
    }

    @Test
    void testGetTaskById_AccessDenied() {
        User user = User.builder()
                .email("test@mock.com")
                .build();
        User other = User.builder()
                .email("other@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.findById(9L))
                .thenReturn(Optional.of(Task.builder()
                        .id(9L)
                        .user(other)
                        .build()));

        assertThrows(CustomException.class, () -> taskService.getTaskById(9L, "test@mock.com"));
    }

    @Test
    void testUpdateById_Success() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        Task existing = Task.builder()
                .id(10L)
                .title("OldTitle")
                .user(mockUser)
                .build();
        when(taskRepository.findById(10L)).thenReturn(Optional.of(existing));

        Category newCat = Category.builder()
                .id(777L)
                .user(mockUser)
                .build();
        when(categoryRepository.findById(777L)).thenReturn(Optional.of(newCat));

        TaskRequest request = TaskRequest.builder()
                .title("UpdatedTitle")
                .categoryId(777L)
                .build();

        Task updatedTask = Task.builder()
                .id(10L)
                .title("UpdatedTitle")
                .category(newCat)
                .user(mockUser)
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskDto mappedDto =  TaskDto.builder()
                .id(10L)
                .title("UpdatedTitle")
                .build();
        when(mapper.mapTo(updatedTask)).thenReturn(mappedDto);

        TaskDto dto = taskService.updateById(10L, request, "test@mock.com");
        assertEquals("UpdatedTitle", dto.getTitle());
        assertEquals(10L, dto.getId());
    }

    @Test
    void testUpdateById_NoSuchTask() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(taskRepository.findById(90L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> taskService.updateById(90L, TaskRequest.builder().build(), "test@mock.com"));
    }

    @Test
    void testUpdateById_AccessDenied() {
        User myUser = User.builder()
                .email("test@mock.com")
                .build();
        User other = User.builder()
                .email("other@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com"))
                .thenReturn(Optional.of(myUser));

        Task otherTask = Task.builder()
                .id(100L)
                .user(other)
                .build();
        when(taskRepository.findById(100L)).thenReturn(Optional.of(otherTask));

        assertThrows(CustomException.class,
                () -> taskService.updateById(100L, TaskRequest.builder().build(), "test@mock.com"));
    }

    @Test
    void testUpdateById_NoSuchUserForUpdate() {
        when(userRepository.findByEmail("missing@mock.com")).thenReturn(Optional.empty());

        TaskRequest request = TaskRequest.builder()
                .title("UpdatedTitle")
                .build();

        CustomException ex = assertThrows(CustomException.class,
                () -> taskService.updateById(1L, request, "missing@mock.com"));
        assertEquals(BusinessErrorCodes.NO_SUCH_EMAIL, ex.getErrorCode());
    }

    @Test
    void testUpdateById_NoSuchCategory() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        Task existing = Task.builder()
                .id(10L)
                .title("OldTitle")
                .user(mockUser)
                .build();
        when(taskRepository.findById(10L)).thenReturn(Optional.of(existing));

        when(categoryRepository.findById(777L)).thenReturn(Optional.empty());

        TaskRequest request = TaskRequest.builder()
                .title("UpdatedTitle")
                .categoryId(777L)
                .build();

        CustomException ex = assertThrows(CustomException.class,
                () -> taskService.updateById(10L, request, "test@mock.com"));
        assertEquals(BusinessErrorCodes.NO_SUCH_CATEGORY, ex.getErrorCode());
    }

    @Test
    void testDeleteById_Success() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        Task existing = Task.builder()
                .id(500L)
                .user(mockUser)
                .build();
        when(taskRepository.findById(500L)).thenReturn(Optional.of(existing));

        taskService.deleteById(500L, "test@mock.com");
        verify(taskRepository).deleteById(500L);
    }

    @Test
    void testDeleteById_AccessDenied() {
        User myUser = User.builder()
                .email("test@mock.com")
                .build();
        User other = User.builder()
                .email("other@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(myUser));

        Task othersTask = Task.builder()
                .id(600L)
                .user(other)
                .build();
        when(taskRepository.findById(600L)).thenReturn(Optional.of(othersTask));

        assertThrows(CustomException.class, () -> taskService.deleteById(600L, "test@mock.com"));
    }

    @Test
    void testDeleteById_NoSuchTask() {
        User mockUser = User.builder()
                .email("test@mock.com")
                .build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        when(taskRepository.findById(777L)).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> taskService.deleteById(777L, "test@mock.com"));
    }
}
