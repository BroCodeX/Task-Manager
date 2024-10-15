package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class TaskUpdateDTO {
    @NotBlank
    private JsonNullable<String> title;

    private JsonNullable<String> content;
    private JsonNullable<String> status;

    private JsonNullable<String> label;
}
