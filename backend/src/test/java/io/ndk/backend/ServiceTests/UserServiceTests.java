package io.ndk.backend.ServiceTests;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.request.AccountSignUp;
import io.ndk.backend.dto.request.SignInRequest;
import io.ndk.backend.dto.response.SignInResponse;
import io.ndk.backend.entity.User;
import io.ndk.backend.handler.BusinessErrorCodes;
import io.ndk.backend.handler.CustomException;
import io.ndk.backend.repository.UserRepository;
import io.ndk.backend.service.JwtService;
import io.ndk.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Mapper<User, AccountSignUp> mapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSignUp_Success() {
        AccountSignUp signUpDto = new AccountSignUp();
        signUpDto.setEmail("new@user.com");
        signUpDto.setPassword("plainpass");

        when(userRepository.findByEmail("new@user.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainpass")).thenReturn("encoded-pass");
        User savedUser = User.builder().id(1L).email("new@user.com").password("encoded-pass").build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AccountSignUp mapped = new AccountSignUp();
        mapped.setEmail("new@user.com");
        mapped.setPassword("encoded-pass");
        when(mapper.mapTo(savedUser)).thenReturn(mapped);

        AccountSignUp result = userService.signUp(signUpDto);

        assertNotNull(result);
        assertEquals("new@user.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSignUp_EmailIsUsed() {
        AccountSignUp signUpDto = new AccountSignUp();
        signUpDto.setEmail("exists@user.com");
        signUpDto.setPassword("secret");

        User existingUser = User.builder().email("exists@user.com").build();
        when(userRepository.findByEmail("exists@user.com")).thenReturn(Optional.of(existingUser));

        CustomException ex = assertThrows(CustomException.class, () -> userService.signUp(signUpDto));
        assertEquals(BusinessErrorCodes.EMAIL_IS_USED, ex.getErrorCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSignIn_Success() {
        SignInRequest request = SignInRequest.builder()
                .email("user@mock.com")
                .password("pass")
                .build();

        User existing = User.builder().id(2L).email("user@mock.com").password("encoded").build();
        when(userRepository.findByEmail("user@mock.com")).thenReturn(Optional.of(existing));

        Authentication authResult = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authResult);
        when(authResult.isAuthenticated()).thenReturn(true);

        SignInResponse response = userService.signIn(request);

        assertNotNull(response);
        assertEquals("user@mock.com", response.getEmail());
        assertEquals(2L, response.getId());
    }

    @Test
    void testSignIn_NoSuchEmail() {
        SignInRequest request = SignInRequest.builder()
                .email("missing@user.com")
                .password("secret")
                .build();

        when(userRepository.findByEmail("missing@user.com")).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> userService.signIn(request));
        assertEquals(BusinessErrorCodes.NO_SUCH_EMAIL, ex.getErrorCode());
    }

    @Test
    void testSignIn_BadCredentials() {
        SignInRequest signInRequest = SignInRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        User userEntity = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(userEntity));

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        CustomException ex = assertThrows(CustomException.class, () -> userService.signIn(signInRequest));
        assertEquals(BusinessErrorCodes.BAD_CREDENTIALS, ex.getErrorCode());
    }

    @Test
    void testLogout_Success() {
        User existing = User.builder().email("logout@mock.com").build();
        when(userRepository.findByEmail("logout@mock.com")).thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> userService.logout("logout@mock.com"));
        verify(userRepository).save(existing);
    }

    @Test
    void testLogout_NoSuchEmail() {
        when(userRepository.findByEmail("unknown@mock.com")).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> userService.logout("unknown@mock.com"));
        assertEquals(BusinessErrorCodes.NO_SUCH_EMAIL, ex.getErrorCode());
        verify(userRepository, never()).save(any(User.class));
    }
}
