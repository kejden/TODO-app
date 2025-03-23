package io.ndk.backend.ServiceTests;

import io.ndk.backend.Mappers.impl.CategoryMapper;
import io.ndk.backend.dto.CategoryDto;
import io.ndk.backend.dto.request.CategoryRequest;
import io.ndk.backend.entity.Category;
import io.ndk.backend.entity.User;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategoriesOfUser() {
        User mockUser = User.builder().email("test@mock.com").build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findByUser(mockUser)).thenReturn(Collections.emptyList());

        assertTrue(categoryService.getAllCategoriesOfUser("test@mock.com").isEmpty());
        verify(userRepository).findByEmail("test@mock.com");
    }

    @Test
    void testGetAllCategoriesOfUser_NotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> categoryService.getAllCategoriesOfUser("unknown@mail.com"));
    }

    @Test
    void testGetCategoryById_Success() {
        Principal principal = () -> "tester@mock.com";
        User mockUser = User.builder().email("tester@mock.com").build();
        when(userRepository.findByEmail("tester@mock.com")).thenReturn(Optional.of(mockUser));

        Category category = Category.builder().id(1L).name("Some Cat").user(mockUser).build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto mappedDto = new CategoryDto();
        mappedDto.setId(1L);
        mappedDto.setName("Some Cat");
        when(mapper.mapTo(category)).thenReturn(mappedDto);

        CategoryDto dto = categoryService.getCategoryById(1L, principal);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Some Cat", dto.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        Principal principal = () -> "tester@mock.com";
        when(userRepository.findByEmail("tester@mock.com"))
                .thenReturn(Optional.of(User.builder().email("tester@mock.com").build()));

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> categoryService.getCategoryById(99L, principal));
    }

    @Test
    void testGetCategoryById_AccessDenied() {
        Principal principal = () -> "test@mock.com";
        when(userRepository.findByEmail("test@mock.com"))
                .thenReturn(Optional.of(User.builder().email("test@mock.com").build()));

        Category otherUserCategory = Category.builder()
                .id(2L)
                .user(User.builder().email("other@mock.com").build())
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(otherUserCategory));
        assertThrows(CustomException.class, () -> categoryService.getCategoryById(2L, principal));
    }

    @Test
    void testAddCategory_AlreadyExists() {
        User mockUser = User.builder().email("test@mock.com").build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        CategoryRequest request = new CategoryRequest("Existent Cat");
        when(categoryRepository.existsByNameAndUser("Existent Cat", mockUser)).thenReturn(true);

        assertThrows(CustomException.class, () -> categoryService.addCategory(request, "test@mock.com"));
    }

    @Test
    void testAddCategory_Success() {
        User mockUser = User.builder().email("test@mock.com").build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));

        CategoryRequest request = new CategoryRequest("New Cat");
        when(categoryRepository.existsByNameAndUser("New Cat", mockUser)).thenReturn(false);

        Category savedCategory = Category.builder().id(10L).name("New Cat").user(mockUser).build();
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDto mappedDto = new CategoryDto();
        mappedDto.setId(10L);
        mappedDto.setName("New Cat");
        when(mapper.mapTo(savedCategory)).thenReturn(mappedDto);

        CategoryDto dto = categoryService.addCategory(request, "test@mock.com");
        assertEquals("New Cat", dto.getName());
        assertEquals(10L, dto.getId());
    }

    @Test
    void testUpdateCategory_Success() {
        Principal principal = () -> "test@mock.com";
        User mockUser = User.builder().email("test@mock.com").build();
        Category originalCategory = Category.builder().id(11L).name("Old Cat").user(mockUser).build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(11L)).thenReturn(Optional.of(originalCategory));

        CategoryRequest updateRequest = new CategoryRequest("Updated Cat");
        Category updatedCategory = Category.builder().id(11L).name("Updated Cat").user(mockUser).build();
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryDto mappedDto = CategoryDto.builder()
                .id(11L)
                .name("Updated Cat")
                .build();
        when(mapper.mapTo(updatedCategory)).thenReturn(mappedDto);

        CategoryDto dto = categoryService.updateCategory(11L, updateRequest, principal);
        assertEquals("Updated Cat", dto.getName());
        assertEquals(11L, dto.getId());
    }

    @Test
    void testUpdateCategory_NoSuchCategory() {
        Principal principal = () -> "test@mock.com";
        when(userRepository.findByEmail("test@mock.com"))
                .thenReturn(Optional.of(User.builder().email("test@mock.com").build()));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> categoryService.updateCategory(99L, new CategoryRequest("Any"), principal));
    }

    @Test
    void testUpdateCategory_AccessDenied() {
        Principal principal = () -> "test@mock.com";
        User mockUser = User.builder().email("test@mock.com").build();
        Category originalCategory = Category.builder()
                .id(11L)
                .name("Old Cat")
                .user(User.builder().email("user@mock.com").build())
                .build();

        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(11L)).thenReturn(Optional.of(originalCategory));

        CategoryRequest updateRequest = new CategoryRequest("Updated Cat");

        assertThrows(
                CustomException.class,
                () -> categoryService.updateCategory(11L, updateRequest, principal)
        );
    }

    @Test
    void testUpdateCategory_OtherUserAccessDenied() {
        Principal principal = () -> "notOwner@mock.com";
        User mockUser = User.builder().email("notOwner@mock.com").build();
        when(userRepository.findByEmail("notOwner@mock.com")).thenReturn(Optional.of(mockUser));

        // Category belongs to someone else
        Category category = Category.builder()
                .id(20L)
                .user(User.builder().email("someOwner@mock.com").build())
                .build();
        when(categoryRepository.findById(20L)).thenReturn(Optional.of(category));

        assertThrows(CustomException.class, () -> categoryService.updateCategory(
                20L, new CategoryRequest("Attempted Update"), principal
        ));
    }

    @Test
    void testDeleteCategory_Success() {
        Principal principal = () -> "test@mock.com";
        User mockUser = User.builder().email("test@mock.com").build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.existsById(88L)).thenReturn(true);

        Category category = Category.builder()
                .id(88L)
                .user(mockUser)
                .build();
        when(categoryRepository.findById(88L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(88L, principal);
        verify(categoryRepository).deleteById(88L);
    }

    @Test
    void testDeleteCategory_NoSuchCategory() {
        Principal principal = () -> "test@mock.com";
        when(userRepository.findByEmail("test@mock.com"))
                .thenReturn(Optional.of(User.builder().email("test@mock.com").build()));
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(CustomException.class, () -> categoryService.deleteCategory(99L, principal));
    }

    @Test
    void testDeleteCategory_AccessDenied() {
        Principal principal = () -> "test@mock.com";
        User mockUser = User.builder().email("test@mock.com").build();
        when(userRepository.findByEmail("test@mock.com")).thenReturn(Optional.of(mockUser));
        when(categoryRepository.existsById(88L)).thenReturn(true);

        Category category = Category.builder()
                .id(88L)
                .user(User.builder().email("someoneelse@mock.com").build())
                .build();
        when(categoryRepository.findById(88L)).thenReturn(Optional.of(category));

        assertThrows(CustomException.class, () -> categoryService.deleteCategory(88L, principal));
    }

    @Test
    void testDeleteCategory_OtherUserAccessDenied() {
        Principal principal = () -> "notOwner@mock.com";
        when(userRepository.findByEmail("notOwner@mock.com"))
                .thenReturn(Optional.of(User.builder().email("notOwner@mock.com").build()));

        Category category = Category.builder()
                .id(21L)
                .user(User.builder().email("another@mock.com").build())
                .build();
        when(categoryRepository.existsById(21L)).thenReturn(true);
        when(categoryRepository.findById(21L)).thenReturn(Optional.of(category));

        assertThrows(CustomException.class, () -> categoryService.deleteCategory(21L, principal));
    }
}
