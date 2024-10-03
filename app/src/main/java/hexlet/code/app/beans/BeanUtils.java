package hexlet.code.app.beans;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils {
    @Bean
    public Faker initFaker() {
        return new Faker();
    }
}
