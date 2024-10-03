package hexlet.code.app.service;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundExcepiton;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hexlet.code.app.dto.UserDTO;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }


    public UserDTO show(Long id) {
        var maybeUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This " + id + " not found"));
        return mapper.map(maybeUser);
    }


    public UserDTO create(UserCreateDTO dto) {
        var user = mapper.map(dto);
        userRepository.save(user);
        return mapper.map(user);
    }


    public UserDTO update(UserUpdateDTO dto, long id) {
        var maybeUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This " + id + " not found"));
        mapper.update(dto, maybeUser);
        userRepository.save(maybeUser);
        return mapper.map(maybeUser);
    }


    public void destroy(long id) {
        userRepository.deleteById(id);
    }
}
