package io.ndk.backend.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    private String title;
    private String description;
    private Long categoryId;
}
