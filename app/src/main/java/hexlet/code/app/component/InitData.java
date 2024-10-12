package hexlet.code.app.component;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initUser();
        initSlug();
    }

    public void initUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("hexlet@example.com");
        dto.setPassword("qwerty");
        userService.create(dto);
        var hexletUser = userRepository.findByEmail("hexlet@example.com").get();
        System.out.println("Init user: " + hexletUser + " created");
    }

    public void initSlug() {
        List<String> slugs = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");
        slugs.forEach(slug -> {
            var status = new Status();
            status.setSlug(slug);
            statusRepository.save(status);
        });
        List<String> checkSlugs = statusRepository.findAll().stream()
                .map(Status::getSlug)
                .toList();
        System.out.println("Init slugs: " + checkSlugs + " created");
    }
}
