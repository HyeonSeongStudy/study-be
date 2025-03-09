package project.studyproject.domain.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostDocWriteRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}
