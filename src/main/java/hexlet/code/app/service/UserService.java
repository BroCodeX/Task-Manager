package hexlet.code.app.service;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundExcepiton;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import hexlet.code.app.dto.user.UserDTO;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll(int limit) {
        return userRepository.findAll().stream()
                .limit(limit)
                .map(mapper::map)
                .toList();
    }


    public UserDTO showUser(Long id) {
        var maybeUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This id: " + id + " is not found"));
        return mapper.map(maybeUser);
    }


    public UserDTO createUser(UserCreateDTO dto) {
        var hashedPass = passwordEncoder.encode(dto.getPassword());
        var user = mapper.map(dto);
        user.setPassword(hashedPass);
        userRepository.save(user);
        return mapper.map(user);
    }


    public UserDTO updateUser(UserUpdateDTO dto, long id) {
        var maybeUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This id: " + id + " is not found"));
        if (dto.getPassword() != null && dto.getPassword().isPresent()) {
            var hashedPass = passwordEncoder.encode(dto.getPassword().get());
            dto.setPassword(JsonNullable.of(hashedPass));
        }
        mapper.update(dto, maybeUser);
        userRepository.save(maybeUser);
        return mapper.map(maybeUser);
    }

    public void destroyUser(long id) {
        userRepository.deleteById(id);
    }
}
