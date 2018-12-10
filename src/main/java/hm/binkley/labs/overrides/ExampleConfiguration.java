package hm.binkley.labs.overrides;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;

@Configuration
@EnableJdbcRepositories
@Import(JdbcConfiguration.class)
public class ExampleConfiguration {}
