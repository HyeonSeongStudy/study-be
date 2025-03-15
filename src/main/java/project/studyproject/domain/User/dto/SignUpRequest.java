package project.studyproject.domain.User.dto;

import org.hibernate.validator.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank
        String id,

        @NotBlank
        String password,

        @NotBlank
        String name
) {


}
