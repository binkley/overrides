package hm.binkley.labs.overrides;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table("OVERRIDES.p_Example_overrides")
public final class ExampleOverrideWrite {
    @Id
    private Long id;
    private Long exampleId;
    private String name;
}
