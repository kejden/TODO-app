package io.ndk.backend.dto.request;

import io.ndk.backend.entity.TaskStatus;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank
    @Length(min=2, max = 15)
    private String title;
    @NotBlank
    @Length(min=2, max = 50)
    private String description;
    @NotBlank
    private TaskStatus status;
    @NotBlank
    private Long categoryId;
}
