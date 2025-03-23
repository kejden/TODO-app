package io.ndk.backend.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ndk.backend.Mappers.impl.TaskMapper;
import io.ndk.backend.config.JwtFilter;
import io.ndk.backend.controller.TaskController;
import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.dto.request.TaskRequest;
import io.ndk.backend.entity.TaskStatus;
import io.ndk.backend.handler.BusinessErrorCodes;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.service.CookieService;
import io.ndk.backend.service.JwtService;
import io.ndk.backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(JwtFilter.class)
@ActiveProfiles("test")
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private CookieService cookieService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private io.ndk.backend.service.impl.customUserDetailService customUserDetailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Principal principal;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        principal = () -> "test@mock.com";
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetTasks() throws Exception {
        List<TaskDto> mockTasks = Arrays.asList(
                TaskDto.builder().id(1L).title("Task One").build(),
                TaskDto.builder().id(2L).title("Task Two").build()
        );
        when(taskService.getTaskByUser("test@mock.com")).thenReturn(mockTasks);

        mockMvc.perform(get("/api/tasks").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Task One"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Task Two"));
    }

    @Test
    void testGetTasksWithoutCategory() throws Exception {
        TaskDto noCatTask = TaskDto.builder().id(10L).title("NoCat").build();
        when(taskService.getTasksWithoutCategory("test@mock.com"))
                .thenReturn(Collections.singletonList(noCatTask));

        mockMvc.perform(get("/api/tasks/category-none").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].title").value("NoCat"));
    }

    @Test
    void testGetTasksByCategory() throws Exception {
        TaskDto catTask = TaskDto.builder().id(99L).title("Task in Cat#5").build();
        when(taskService.getTaskByCategoryId(5L, "test@mock.com"))
                .thenReturn(Collections.singletonList(catTask));

        mockMvc.perform(get("/api/tasks/category/5").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(99L))
                .andExpect(jsonPath("$[0].title").value("Task in Cat#5"));
    }

    @Test
    void testGetTaskById() throws Exception {
        TaskDto found = TaskDto.builder().id(123L).title("Found Task").build();
        when(taskService.getTaskById(123L, "test@mock.com")).thenReturn(found);

        mockMvc.perform(get("/api/tasks/123").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.title").value("Found Task"));
    }

    @Test
    void testCreateTask() throws Exception {
        TaskRequest requestBody = TaskRequest.builder()
                .title("New Task")
                .description("New Desc")
                .categoryId(null)
                .status(TaskStatus.NEW)
                .build();

        TaskDto createdDto = TaskDto.builder()
                .id(555L)
                .title("New Task")
                .description("New Desc")
                .status("NEW")
                .build();
        when(taskService.createTask(any(TaskRequest.class), eq("test@mock.com")))
                .thenReturn(createdDto);

        mockMvc.perform(post("/api/tasks")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(555))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Desc"));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskRequest updateRequest = TaskRequest.builder()
                .title("Updated Title")
                .description("Updated Desc")
                .categoryId(99L)
                .status(TaskStatus.IN_PROGRESS)
                .build();

        TaskDto updatedDto = TaskDto.builder()
                .id(777L)
                .title("Updated Title")
                .description("Updated Desc")
                .status("IN_PROGRESS")
                .build();
        when(taskService.updateById(eq(777L), any(TaskRequest.class), eq("test@mock.com")))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/tasks/777")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Desc"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/999").principal(principal))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTask_AccessDenied() throws Exception {
        doThrow(new CustomException(BusinessErrorCodes.ACCESS_DENIED)).when(taskService).deleteById(1L, principal.getName());

        mockMvc.perform(delete("/api/tasks/1").principal(principal))
                .andExpect(status().isForbidden());
    }

}
