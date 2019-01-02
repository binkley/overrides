package hm.binkley.labs.overrides;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ContextConfiguration(classes = ExampleConfiguration.class)
@JdbcTest
public class ExampleRepositoryTest {
    @Autowired
    private ExampleRepository repository;
    @Autowired
    private ExampleWriteRepository writeRepository;
    @Autowired
    private ExampleOverrideWriteRepository overrideRepository;

    @Test
    public void shouldOverride() {
        final Iterable<Example> found = repository.findAll();

        assertThat(found).isEmpty();

        final ExampleWrite bob = writeRepository
                .save(new ExampleWrite(null, "Bob"));
        final ExampleWrite nancy = writeRepository
                .save(new ExampleWrite(null, "Nancy"));
        final ExampleOverrideWrite fred = overrideRepository
                .save(bob.withOverride("Fred"));

        assertThat(repository.findById(bob.getId())).isEqualTo(Optional.of(
                Example.builder().id(bob.getId()).name(fred.getName())
                        .overrideId(fred.getId()).build()));
        assertThat(repository.findById(nancy.getId())).isEqualTo(Optional.of(
                Example.builder().id(nancy.getId()).name(nancy.getName())
                        .overrideId(null).build()));
        assertThat(writeRepository.findByName(bob.getName())).isEqualTo(
                Optional.of(ExampleWrite.builder().id(bob.getId())
                        .name(bob.getName()).build()));
    }

    @Test
    public void shouldPreventDuplicateOverrides() {
        final ExampleWrite bob = writeRepository
                .save(new ExampleWrite(null, "Bob"));
        overrideRepository
                .save(new ExampleOverrideWrite(null, bob.getId(), "Fred"));

        final Throwable thrown = catchThrowable(() -> overrideRepository
                .save(new ExampleOverrideWrite(null, bob.getId(), "Howard")));
        final Optional<Throwable> expected = Stream
                .iterate(thrown, Objects::nonNull, Throwable::getCause)
                .filter(t -> t instanceof DuplicateKeyException).findFirst();
        assertThat(expected).isNotEmpty();
    }
}
