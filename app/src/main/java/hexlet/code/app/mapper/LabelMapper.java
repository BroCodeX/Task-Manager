package hexlet.code.app.mapper;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.model.Label;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public abstract class LabelMapper {
    public abstract LabelDTO map(Label label);

    public abstract Label map(LabelDTO dto);

    public abstract Label map(LabelCreateDTO dto);

    public abstract void update(LabelUpdateDTO dto, @MappingTarget Label label);
}
