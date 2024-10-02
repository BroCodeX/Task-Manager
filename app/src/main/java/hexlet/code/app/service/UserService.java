package hexlet.code.app.service;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserUpdateDTO;
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

    public List<UserDTO> index() {

    }


    public UserDTO show(Long id) {

    }


    public UserDTO create(UserCreateDTO dto) {

    }


    public UserDTO update(UserUpdateDTO dto, long id) {

    }


    public void destroy(long id) {
        userRepository.deleteById(id);
    }
}
