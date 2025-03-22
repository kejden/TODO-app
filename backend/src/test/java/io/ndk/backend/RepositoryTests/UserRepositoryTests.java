package io.ndk.backend.RepositoryTests;

import io.ndk.backend.entity.User;
import io.ndk.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByEmail() {
        String email = "test@example.com";
        String password = "secret123";

        User user = User.builder()
                .id(0L)
                .email(email)
                .password(password)
                .build();

        User saved = userRepository.save(user);
        assertNotNull(saved.getId(), "Saved user should have an ID");

        User found = userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(found, "User should be found by email");
    }
}
