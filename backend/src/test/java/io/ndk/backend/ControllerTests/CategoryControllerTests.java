package io.ndk.backend.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ndk.backend.Mappers.impl.CategoryMapper;
import io.ndk.backend.config.JwtFilter;
import io.ndk.backend.controller.CategoryController;
import io.ndk.backend.dto.CategoryDto;
import io.ndk.backend.dto.request.CategoryRequest;
import io.ndk.backend.entity.Category;
import io.ndk.backend.handler.BusinessErrorCodes;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.service.CategoryService;
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
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(JwtFilter.class)
@ActiveProfiles("test")
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryMapper categoryMapper;

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
    void testGetCategories() throws Exception {
        List<CategoryDto> categories = Arrays.asList(
                CategoryDto.builder().id(1L).name("Home").build(),
                CategoryDto.builder().id(2L).name("Work").build()
        );

        when(categoryService.getAllCategoriesOfUser(principal.getName())).thenReturn(categories);

        mockMvc.perform(get("/api/categories")
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Home"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Work"));
    }

    @Test
    void testGetCategory() throws Exception {
        CategoryDto category = CategoryDto.builder().id(1L).name("Home").build();

        when(categoryService.getCategoryById(1L, principal)).thenReturn(category);

        mockMvc.perform(get("/api/categories/1").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Home"));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDto category = CategoryDto.builder().id(1L).name("Home").build();
        CategoryRequest categoryRequest = CategoryRequest.builder().name("Home").build();

        when(categoryService.addCategory(categoryRequest, principal.getName())).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Home"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        CategoryDto category = CategoryDto.builder().id(1L).name("Home2").build();
        CategoryRequest categoryRequest = CategoryRequest.builder().name("Home2").build();

        when(categoryService.updateCategory(1L, categoryRequest, principal)).thenReturn(category);

        mockMvc.perform(put("/api/categories/1")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Home2"));
    }

    @Test
    void testUpdateCategory_AccessDenied() throws Exception {
        CategoryRequest categoryRequest = CategoryRequest.builder().name("Home2").build();

        when(categoryService.updateCategory(1L, categoryRequest, principal))
                .thenThrow(new CustomException(BusinessErrorCodes.ACCESS_DENIED));

        mockMvc.perform(put("/api/categories/1")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1").principal(principal))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategory_AccessDenied() throws Exception {
        doThrow(new CustomException(BusinessErrorCodes.ACCESS_DENIED)).when(categoryService).deleteCategory(1L, principal);

        mockMvc.perform(delete("/api/categories/1").principal(principal))
                .andExpect(status().isForbidden());
    }
}