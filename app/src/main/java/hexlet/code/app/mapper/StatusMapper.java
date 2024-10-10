package hexlet.code.app.mapper;

import hexlet.code.app.dto.StatusCreateDTO;
import hexlet.code.app.dto.StatusDTO;
import hexlet.code.app.dto.StatusUpdateDTO;
import hexlet.code.app.model.Status;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public abstract class StatusMapper {
    public abstract StatusDTO map(Status status);
    public abstract Status map(StatusDTO dto);
    public abstract Status map(StatusCreateDTO dto);

    public abstract void update(StatusUpdateDTO dto, @MappingTarget Status status);
}
