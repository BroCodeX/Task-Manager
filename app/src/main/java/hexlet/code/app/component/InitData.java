package hexlet.code.app.component;

import hexlet.code.app.dto.StatusCreateDTO;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.StatusService;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private StatusService statusService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initUser();
        initStatuses();
    }

    public void initUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("hexlet@example.com");
        dto.setPassword("qwerty");
        userService.create(dto);
        var hexletUser = userRepository.findByEmail("hexlet@example.com").get();
        System.out.println("Init user: " + hexletUser + " created");
    }

    public void initStatuses() {
        List<String> slugs = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");
        List<String> titles = List.of("New", "In Progress", "On Testing", "Completed", "Blocked");

        List<StatusCreateDTO> statusList = titles.stream()
                .map(s -> {
                    var dto = new StatusCreateDTO();
                    dto.setName(s);
                    return dto;
                })
                .toList();

        statusList.forEach(statusService::create);
        List<String> checkTitles = statusRepository.findAll().stream()
                .map(Status::getName)
                .toList();
        System.out.println("Init statuses: " + checkTitles + " created");
    }
}
