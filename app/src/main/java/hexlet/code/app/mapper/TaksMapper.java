package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public abstract class TaksMapper {

    @Mapping(source = "assignee_id", target = "assignee.id")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus.name")
    public abstract Task map(TaskDTO dto);

    @Mapping(target = "assignee_id", source = "assignee.id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.name")
    public abstract TaskDTO map(Task task);

    @Mapping(source = "assignee_id", target = "assignee.id")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus.name")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus.name")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);
}
