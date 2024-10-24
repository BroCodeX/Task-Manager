package hexlet.code.app.mapper;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.model.User;

import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
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

    public abstract User map(UserCreateDTO dto);

    public abstract void update(UserUpdateDTO dto, @MappingTarget User user);

    @BeforeMapping // Marks a method to be invoked at the beginning of a generated mapping method.
    public void toHashPass(UserCreateDTO dto) {
        if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
            var encodedPass = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(encodedPass);
        }
    }

    @BeforeMapping
    public void toHashPass(UserUpdateDTO dto) {
        if(dto.getPassword() != null && dto.getPassword().isPresent()) {
            var encodedPass = passwordEncoder.encode(dto.getPassword().get());
            dto.setPassword(JsonNullable.of(encodedPass));
        }
    }
}
