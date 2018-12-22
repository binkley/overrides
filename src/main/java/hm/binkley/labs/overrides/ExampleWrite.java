package hm.binkley.labs.overrides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Builder
@Data
@Table("OVERRIDES.p_Examples")
public final class ExampleWrite {
    @Id
    private Long id;
    private String name;

    public ExampleOverrideWrite withOverride(final String name) {
        return new ExampleOverrideWrite(null, id, name);
    }
}
