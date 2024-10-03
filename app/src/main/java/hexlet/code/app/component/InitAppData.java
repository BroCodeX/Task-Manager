package hexlet.code.app.component;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitAppData implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("hexlet@example.com");
        dto.setPassword(passwordEncoder.encode("qwerty"));
        User user = mapper.map(dto);
        userRepository.save(user);
    }
}
