package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class TaskUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<String> status;
}
