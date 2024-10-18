package hexlet.code.app.mapper;

import hexlet.code.app.dto.status.StatusCreateDTO;
import hexlet.code.app.dto.status.StatusDTO;
import hexlet.code.app.dto.status.StatusUpdateDTO;
import hexlet.code.app.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

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
