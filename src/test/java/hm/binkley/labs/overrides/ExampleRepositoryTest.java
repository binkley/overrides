package hm.binkley.labs.overrides;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = ExampleConfiguration.class)
@JdbcTest
public class ExampleRepositoryTest {
    @Autowired
    private ExampleRepository repository;

    @Test
    public void shouldFindAll() {
        final Iterable<Example> found = repository.findAll();

        assertThat(found.iterator().hasNext(), is(false));
    }
}
