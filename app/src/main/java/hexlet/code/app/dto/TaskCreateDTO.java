package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskCreateDTO {
    private Integer index;
    private Long assignee_id;
    private String title;
    private String content;
    private String status;
}
