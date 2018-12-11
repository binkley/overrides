package hm.binkley.labs.overrides;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ExampleConfiguration.class)
@JdbcTest
public class ExampleRepositoryTest {
    @Autowired
    private ExampleRepository repository;

    @Test
    public void shouldRoundTrip() {
        final Iterable<Example> found = repository.findAll();

        assertThat(found).isEmpty();

        final Example saved = repository.save(new Example(null, "Bob"));

        assertThat(repository.findById(saved.getId()))
                .isEqualTo(Optional.of(saved));
    }
}
