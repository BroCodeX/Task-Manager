package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<Long> taskLabelIds;
}
