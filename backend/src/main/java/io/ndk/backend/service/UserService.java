package io.ndk.backend.service;

import io.ndk.backend.dto.request.AccountSignUp;
import io.ndk.backend.dto.request.SignInRequest;
import io.ndk.backend.dto.response.SignInResponse;

public interface UserService {
    AccountSignUp signUp(AccountSignUp dto);
    SignInResponse signIn(SignInRequest dto);
    void logout(String email);
}
