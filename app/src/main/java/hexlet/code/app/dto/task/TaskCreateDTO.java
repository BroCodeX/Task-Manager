package hexlet.code.app.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


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

    private List<String> labels;
}
