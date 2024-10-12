package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskCreateDTO {
    private Integer index;
    private Long assignee_id;
    private String content;

    @NotBlank
    @Size(min = 1)
    private String title;

    @NotBlank
    private String status;
}
