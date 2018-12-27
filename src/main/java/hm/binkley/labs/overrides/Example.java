package hm.binkley.labs.overrides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Builder
@Data
@Table("OVERRIDES.Examples")
public final class Example {
    @Id
    private Long id;
    private String name;
    private Long overrideId;

    /** @todo No application property for this? */
    public Long getResourceId() {
        return id;
    }
}
