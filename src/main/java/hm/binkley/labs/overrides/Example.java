package hm.binkley.labs.overrides;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table("PUBLIC.p_Example")
public final class Example {
    @Id
    private Long id;
    private String name;
}
