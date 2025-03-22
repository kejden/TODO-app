package io.ndk.backend.RepositoryTests;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import io.ndk.backend.entity.Category;
import io.ndk.backend.repository.CategoryRepository;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Category saved;
    private User user;

    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        User user = User.builder()
                .email("cat_test@example.com")
                .password("secret")
                .build();
        this.user = userRepository.save(user);

        Category category = Category.builder()
                .name("Groceries")
                .user(user)
                .build();

        this.saved = categoryRepository.save(category);
    }

    @Test
    void testSaveAndFindById() {
        Category found = categoryRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getId(), saved.getId());
        assertEquals(found.getName(), saved.getName());
        assertEquals(found.getUser(), user);
    }

    @Test
    void testSaveAndFindByUser() {
        Category found = categoryRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getId(), saved.getId());
        assertEquals(found.getName(), saved.getName());
        assertEquals(found.getUser(), user);
    }

    @Test

    void testSaveAndFindByName() {
        Category found = categoryRepository.findByName(saved.getName()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getId(), saved.getId());
        assertEquals(found.getName(), saved.getName());
        assertEquals(found.getUser(), user);
    }
}
