package hm.binkley.labs.overrides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Builder
@Data
@Table("OVERRIDES.Example")
public final class Example {
    @Id
    private Long id;
    private String name;
}
