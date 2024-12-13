package io.ndk.backend.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum BusinessErrorCodes {

    NO_CODE(0,"No code", HttpStatus.NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Current password is incorrect", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "New password does not match", HttpStatus.BAD_REQUEST),
    BAD_CREDENTIALS(304, "Login and / or password is incorrect", HttpStatus.FORBIDDEN),
    NO_SUCH_EMAIL(305, "There is no user of such email", HttpStatus.BAD_REQUEST),
    BAD_JWT_TOKEN(306, "Invalid JWT token", HttpStatus.BAD_REQUEST),
    EMAIL_IS_USED(307, "Email is already in used", HttpStatus.BAD_REQUEST),
    NO_SUCH_ID(308, "There is no user with that ID", HttpStatus.BAD_REQUEST),
    BAD_COOKIE(309, "Provided cookie is incorrect", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXISTS(310, "This category already exists", HttpStatus.BAD_REQUEST),
    NO_SUCH_CATEGORY(311, "This category does not exist", HttpStatus.BAD_REQUEST),
    NO_SUCH_TASK(312, "This task does not exist", HttpStatus.BAD_REQUEST),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

}
