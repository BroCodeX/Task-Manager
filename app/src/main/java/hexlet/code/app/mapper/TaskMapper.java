package hexlet.code.app.mapper;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public abstract class TaskMapper {

    @Mapping(source = "assignee_id", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toStatusEntity")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabelEntities")
    public abstract Task map(TaskDTO dto);

    @Mapping(target = "assignee_id", source = "assignee.id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "toLabelNames")
    public abstract TaskDTO map(Task task);

    @Mapping(source = "assignee_id", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toStatusEntity")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabelEntities")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toStatusEntity")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabelEntities")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);
}
