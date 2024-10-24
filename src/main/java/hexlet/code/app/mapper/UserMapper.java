package hexlet.code.app.mapper;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract User map(UserDTO dto);

    public abstract UserDTO map(User user);

    @Mapping(source = "password", target = "password", qualifiedByName = "toHashPass")
    public abstract User map(UserCreateDTO dto);

    @Mapping(source = "password", target = "password", qualifiedByName = "toHashPass")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User user);

    @Named("toHashPass")
    public String toHashPass(String pass) {
        if(pass != null && !pass.isBlank()) {
            return passwordEncoder.encode(pass);
        }
        return pass;
    }
}
