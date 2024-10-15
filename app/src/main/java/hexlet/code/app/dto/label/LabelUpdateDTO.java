package hexlet.code.app.dto.label;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class LabelUpdateDTO {
    private JsonNullable<String> name;
}
