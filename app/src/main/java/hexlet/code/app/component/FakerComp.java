package hexlet.code.app.component;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FakerComp {
    @Bean
    public Faker initFaker() {
        return new Faker();
    }
}
