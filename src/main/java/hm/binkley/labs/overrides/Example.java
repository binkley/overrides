package hm.binkley.labs.overrides;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@RequiredArgsConstructor
@Table("PUBLIC.p_Example")
public final class Example {
    @Id
    private final Long id;
}
