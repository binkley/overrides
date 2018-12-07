package hm.binkley.labs.overrides;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.springframework.boot.SpringApplication.run;

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class Application {
    public static void main(final String... args) {
        run(Application.class, args);
    }
}
