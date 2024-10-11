package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TaskDTO {
    private Long id;
    private Integer index;
    private LocalDate createdAt;
    private Long assignee_id;
    private String title;
    private String content;
    private String status;
}
