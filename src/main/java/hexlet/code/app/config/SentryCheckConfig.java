package hexlet.code.app.config;

import io.sentry.Sentry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryCheckConfig {

    @Bean
    public String checkSentryInitialization() {
        String dsn = Sentry.getCurrentHub().getOptions().getDsn();
        if (dsn != null && !dsn.isEmpty()) {
            System.out.println("Sentry initialized with DSN: " + dsn);
            return "Sentry is started";
        } else {
            System.out.println("Sentry DSN is missing or invalid.");
            return "Sentry is failed";
        }
    }
}