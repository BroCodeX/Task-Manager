package hexlet.code.app.service;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import hexlet.code.app.dto.user.UserDTO;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }


    public UserDTO getUserById(Long id) {
        var maybeUser = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        return mapper.map(maybeUser);
    }


    public UserDTO createUser(UserCreateDTO dto) {
        var user = mapper.map(dto);
        userRepository.save(user);
        return mapper.map(user);
    }


    public UserDTO updateUser(UserUpdateDTO dto, long id) {
        var maybeUser = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        mapper.update(dto, maybeUser);
        userRepository.save(maybeUser);
        return mapper.map(maybeUser);
    }

    public void destroyUser(long id) {
        userRepository.deleteById(id);
    }
}
